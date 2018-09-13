/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.raincat.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.raincat.common.constant.CommonConstant;
import com.raincat.core.concurrent.threadlocal.TxTransactionLocal;

/**
 * DubboTxTransactionFilter.
 * @author xiaoyu
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class DubboTxTransactionFilter implements Filter {

    @Override
    public Result invoke(final Invoker<?> invoker, final Invocation invocation) throws RpcException {
        if (RpcContext.getContext().isConsumerSide()) { // 消费方一般自己生成事务组id或者由上游的消费方传递过来
            RpcContext.getContext().setAttachment(CommonConstant.TX_TRANSACTION_GROUP,
                    TxTransactionLocal.getInstance().getTxGroupId());
        } else { // 如果是提供方那么先将上游传递过来的事务组id设置到本地线程变量，以便继续往下调用时正常传递
            TxTransactionLocal.getInstance().setTxGroupId(RpcContext.getContext().getAttachment(CommonConstant.TX_TRANSACTION_GROUP));
        }
        return invoker.invoke(invocation);
    }
}
