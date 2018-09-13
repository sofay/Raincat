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
package com.raincat.dubbo.sample.consume.service.impl;

import com.raincat.common.holder.IdWorkerUtils;
import com.raincat.core.annotation.TxTransaction;
import com.raincat.dubbo.sample.consume.entity.Test1;
import com.raincat.dubbo.sample.consume.mapper.Test1Mapper;
import com.raincat.dubbo.sample.consume.service.Test1Service;
import com.raincat.dubbo.sample.order.api.entity.Order;
import com.raincat.dubbo.sample.order.api.service.OrderService;
import com.raincat.dubbo.sample.stock.api.entity.Stock;
import com.raincat.dubbo.sample.stock.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author xiaoyu
 */
@Service
public class Test1ServiceImpl implements Test1Service {


    private final Test1Mapper test1Mapper;

    private final OrderService orderService;


    private final StockService stockService;

    @Autowired
    public Test1ServiceImpl(Test1Mapper test1Mapper, OrderService orderService, StockService stockService) {
        this.test1Mapper = test1Mapper;
        this.orderService = orderService;
        this.stockService = stockService;
    }


    /**
     * 正常保存 test1表插入数据，order表插入数据 stock表插入数据
     *
     * @return
     */
    @Override
    @TxTransaction
    public String save() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        orderService.save(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.save(stock);


        return "success";
    }

    @Override
    public String testFail() {
        String name = "FIAL";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        // int i = 100 / 0;
        return "fial";

    }

    /**
     * 强一致性测试
     * 测试 订单保存异常的情况
     * 此时t_test 表不会新增数据 order表不会新增数据 stock则不执行
     *
     * @return "order_fail"
     */
    @Override
    @TxTransaction
    public String testOrderFail() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        orderService.fail(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.save(stock);
        return "order_fail";
    }

    /**
     * 强一致性测试
     * 测试 订单保存超时的情况
     * 此时t_test 表不会新增数据,order表不会新增数据 stock则不执行
     *
     * @return "order_timeOut"
     */
    @Override
    @TxTransaction
    public String testOrderTimeOut() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        orderService.timeOut(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.save(stock);

        return "order_timeOut";
    }

    /**
     * 强一致性测试
     * 测试 stock保存异常的情况
     * 此时t_test 表不会新增数据 order表不会新增数据 stock表不会新增数据
     *
     * @return "stock_fail"
     */
    @Override
    @TxTransaction
    public String testStockFail() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        String obj = orderService.save(order);
        System.err.println(obj);

        Stock stock = new Stock();
        stockService.fail(stock);
        return "stock_fail";
    }

    /**
     * 强一致性测试
     * 测试 stock保存超时的情况
     * 此时t_test 表不会新增数据,order表不会新增数据 stock表不会新增数据
     *
     * @return "stock_timeOut"
     */
    @Override
    @TxTransaction
    public String testStockTimeOut() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        orderService.save(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.timeOut(stock);

        return "stock_timeOut";
    }

    @Override
    @TxTransaction
    public String testOrderInvokeStock() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        orderService.invokeStock(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.save(stock);
        return "testOrderInvokeStock";
    }

    @Override
    @TxTransaction
    public String testOrderInvokeStockFail() {
        String name = "hello_demo1";
        Test1 test = new Test1();
        test.setName(name);
        test1Mapper.save(test);

        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().createUUID());
        order.setStatus(0);
        order.setType(0);
        orderService.invokeStockFail(order);

        Stock stock = new Stock();
        stock.setName(IdWorkerUtils.getInstance().buildPartNumber());
        stock.setNumber(100);
        stock.setCreateTime(new Date());
        stockService.save(stock);
        return "testOrderInvokeStockFail";
    }
}
