package com.example.mes.process.Controller;


import com.alibaba.fastjson.JSON;
import com.example.mes.process.Service.IEquipmentService;
import com.example.mes.process.Vo.EquipmentVo.DeleteEquipmentVo;
import com.example.mes.process.Vo.EquipmentVo.InsertEquipmentVo;
import com.example.mes.process.Vo.EquipmentVo.QueryEquipmentVo;
import com.example.mes.process.Vo.EquipmentVo.UpdateEquipmentVo;
import com.example.mes.process.Vo.PageVo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/process")
public class EquipmentController {

    @Autowired
    IEquipmentService service;

    @GetMapping("/getEquipments")
    public String getEquipments(int pageOffset,int pageSize){
        try {
            HashMap<String,Object> data = new HashMap<>();
            List<QueryEquipmentVo> equipments = service.getEquipments(new PageVo(pageOffset,pageSize));
            int count = service.getCount();
            data.put("count",count);
            data.put("equipments",equipments);
            return JSON.toJSONString(data);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("service:查询设备信息列表失败！");
            return "";
        }
    }

    @GetMapping("/getEquipmentByID")
    public String getEquipmentByID(String equipment_id){
        try {
            return JSON.toJSONString(service.getEquipmentByID(equipment_id));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("service:根据设备编号获取设备信息失败！");
            return "";
        }
    }

    @PostMapping("/addEquipment")
    public String addEquipment(@RequestBody InsertEquipmentVo insertEquipmentVo){
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String status = "正常";
            insertEquipmentVo.setEquipment_id(service.getIndex()+1+"");
            insertEquipmentVo.setCreated_time(timestamp);
            insertEquipmentVo.setStatus(status);
            return service.addEquipment(insertEquipmentVo);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("service:增加设备失败！");
            return "添加失败";
        }
    }

    @PostMapping("/deleteEquipmentByID")
    public String deleteEquipmentByID(@RequestBody DeleteEquipmentVo deleteEquipmentVo){
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            deleteEquipmentVo.setModified_time(timestamp);
            return service.deleteEquipmentByID(deleteEquipmentVo);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("controller:删除设备失败！");
            return "删除失败";
        }
    }

    @PostMapping("/updateEquipmentByID")
    public String updateEquipmentByID(@RequestBody UpdateEquipmentVo updateEquipmentVo){
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            updateEquipmentVo.setModified_time(timestamp);
            return service.updateEquipmentByID(updateEquipmentVo);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("controller:更新设备信息失败！");
            return "更新失败";
        }
    }

}
