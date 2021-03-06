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

package com.raincat.core.service.message;

import com.raincat.common.enums.NettyMessageActionEnum;
import com.raincat.common.enums.TransactionStatusEnum;
import com.raincat.common.netty.bean.RequestPackage;
import com.raincat.common.netty.bean.TxTransactionGroup;
import com.raincat.common.netty.bean.TxTransactionItem;
import com.raincat.core.netty.handler.NettyClientMessageHandler;
import com.raincat.core.service.TxManagerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 * NettyMessageServiceImpl.
 * @author xiaoyu
 */
@Service
public class NettyMessageServiceImpl implements TxManagerMessageService {

    private final NettyClientMessageHandler nettyClientMessageHandler;

    @Autowired
    public NettyMessageServiceImpl(final NettyClientMessageHandler nettyClientMessageHandler) {
        this.nettyClientMessageHandler = nettyClientMessageHandler;
    }

    @Override
    public Boolean createdTxTransactionGroup(final TxTransactionGroup txTransactionGroup) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.CREATE_GROUP.getCode());
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (Boolean) object;
        }
        return false;

    }

    @Override
    public Boolean addTxTransaction(final String txGroupId, final TxTransactionItem txTransactionItem) {
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setId(txGroupId);
        txTransactionGroup.setItemList(Collections.singletonList(txTransactionItem));

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.ADD_TRANSACTION.getCode());
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (Boolean) object;
        }
        return false;
    }

    @Override
    public int findTransactionGroupStatus(final String txGroupId) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.GET_TRANSACTION_GROUP_STATUS.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setId(txGroupId);
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (Integer) object;
        }
        return TransactionStatusEnum.ROLLBACK.getCode();
    }

    @Override
    public TxTransactionGroup findByTxGroupId(final String txGroupId) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.FIND_TRANSACTION_GROUP_INFO.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setId(txGroupId);
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (TxTransactionGroup) object;
        }
        return null;
    }

    @Override
    public void rollBackTxTransaction(final String txGroupId) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.ROLLBACK.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setStatus(TransactionStatusEnum.ROLLBACK.getCode());
        txTransactionGroup.setId(txGroupId);
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
    }

    @Override
    public Boolean preCommitTxTransaction(final String txGroupId) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.PRE_COMMIT.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setStatus(TransactionStatusEnum.PRE_COMMIT.getCode());
        txTransactionGroup.setId(txGroupId);
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (Boolean) object;
        }
        return false;
    }

    @Override
    public Boolean completeCommitTxTransaction(final String txGroupId, final String taskKey, final int status) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.COMPLETE_COMMIT.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setId(txGroupId);
        TxTransactionItem item = new TxTransactionItem();
        item.setTaskKey(taskKey);
        item.setStatus(status);
        txTransactionGroup.setItemList(Collections.singletonList(item));
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        final Object object = nettyClientMessageHandler.sendTxManagerMessage(requestPackage);
        if (Objects.nonNull(object)) {
            return (Boolean) object;
        }
        return false;
    }

    @Override
    public void asyncCompleteCommit(final String txGroupId, final String taskKey, final int status, final Object message) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setAction(NettyMessageActionEnum.COMPLETE_COMMIT.getCode());
        TxTransactionGroup txTransactionGroup = new TxTransactionGroup();
        txTransactionGroup.setId(txGroupId);
        TxTransactionItem item = new TxTransactionItem();
        item.setTaskKey(taskKey);
        item.setStatus(status);
        item.setMessage(message);
        txTransactionGroup.setItemList(Collections.singletonList(item));
        requestPackage.setTxTransactionGroup(txTransactionGroup);
        nettyClientMessageHandler.asyncSendTxManagerMessage(requestPackage);
    }

}
