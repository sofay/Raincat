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

package com.raincat.manager.config;

import com.raincat.common.enums.NettyMessageActionEnum;
import com.raincat.common.enums.TransactionStatusEnum;
import com.raincat.common.netty.bean.RequestPackage;
import com.raincat.common.netty.bean.TxTransactionGroup;
import com.raincat.common.netty.bean.TxTransactionItem;
import com.raincat.manager.socket.SocketManager;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.Objects;

/**
 * ExecutorMessageTool.
 * @author xiaoyu
 */
public class ExecutorMessageTool {

    public static RequestPackage buildMessage(final TxTransactionItem item,
                                              final ChannelSender channelSender,
                                              final TransactionStatusEnum transactionStatusEnum) {
        RequestPackage requestPackage = new RequestPackage();
        Channel channel = SocketManager.getInstance().getChannelByModelName(item.getModelName());
        if (Objects.nonNull(channel)) {
            if (channel.isActive()) {
                channelSender.setChannel(channel);
            }
        }
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        if (TransactionStatusEnum.ROLLBACK.getCode() == transactionStatusEnum.getCode()) {
            requestPackage.setAction(NettyMessageActionEnum.ROLLBACK.getCode());
            item.setStatus(TransactionStatusEnum.ROLLBACK.getCode());
            txTransactionGroup.setStatus(TransactionStatusEnum.ROLLBACK.getCode());
        } else if (TransactionStatusEnum.COMMIT.getCode() == transactionStatusEnum.getCode()) {
            requestPackage.setAction(NettyMessageActionEnum.COMPLETE_COMMIT.getCode());
            item.setStatus(TransactionStatusEnum.COMMIT.getCode());
            txTransactionGroup.setStatus(TransactionStatusEnum.COMMIT.getCode());
        }
        txTransactionGroup.setItemList(Collections.singletonList(item));
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        return requestPackage;
    }
}
