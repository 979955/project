package com.example.mes.process.Service;

import com.example.mes.process.Vo.PageVo.PageVo;
import com.example.mes.process.Vo.ProductVo.DeleteProductVo;

import java.util.HashMap;
import java.util.List;

public interface IProductService {

    //查询所有的产品概述
    HashMap<String,Object> getProducts(PageVo pageVo);
    //逻辑删除一个产品
    String deleteProductByID(DeleteProductVo deleteProductVo);
    //test
    List<String> getProductIdsByDate(String date);


}
