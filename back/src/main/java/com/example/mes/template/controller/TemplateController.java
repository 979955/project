package com.example.mes.template.controller;
import com.example.mes.quality.bean.DefectBean;
import com.example.mes.template.entity.EquipmentTemplate;
import com.example.mes.template.entity.MaterialTemplate;
import com.example.mes.template.service.TemplateService;
import com.example.mes.template.vo.EquipmentTemplateVO;
import com.example.mes.template.vo.MaterialTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;

    @CrossOrigin
    @GetMapping("/material/all")  //全部物料模板信息 格式在下方注释
    public Object materialAll(String company_id) throws Exception {
        List<MaterialTemplate> materialTemplate = templateService.getAllMaterial(company_id);
        HashMap<String,Object> map = new HashMap<>();
        //key为物料名，value为物料属性数组，如：{"拉链":["规格","颜色"],"把手":["颜色","状态","描述"]}

        //id与上一记录不一样就是另一模板，清空list重新加入key value
        for(MaterialTemplate t : materialTemplate) {
            List<String> lst;
            if(map.containsKey(t.getName())){
                lst = (List<String>) map.get(t.getName());
            }else{
                lst = new LinkedList<>();
            }
            lst.add(t.getAttribute());
            map.put(t.getName(),lst);
        }
        System.out.println(company_id);
        return map;
    }

    @CrossOrigin
    @GetMapping("/material/getMaterialTemplateByName")  //全部物料模板信息 格式在下方注释
    public Object getMaterialTemplateByName(String name,String company_id) throws Exception {

        List<MaterialTemplate> materialTemplate = templateService.getMaterialTemplateByName(name,company_id);
        HashMap<String,Object> map = new HashMap<>();

        //key为物料名，value为物料属性数组，如：{"拉链":["规格","颜色"],"把手":["颜色","状态","描述"]}

        for(MaterialTemplate t : materialTemplate) {
            List<String> lst;
            if(map.containsKey(t.getName())){
                lst = (List<String>) map.get(t.getName());
            }else{
                lst = new LinkedList<>();
            }
            lst.add(t.getAttribute());
            map.put(t.getName(),lst);
        }
        System.out.println(company_id);
        return map;
    }


    @CrossOrigin
    @PostMapping("/material/addMaterialTemplate")
    public String addMaterialTemplate(@RequestBody MaterialTemplateVo materialTemplateVo) throws Exception{
         return templateService.addMaterialTemplate(materialTemplateVo);
    }

    @CrossOrigin
    @PostMapping("/material/deleteMaterialTemplate")
    public String deleteMaterialTemplate(@RequestBody MaterialTemplateVo materialTemplateVo) throws Exception{
        return templateService.deleteMaterialTemplate(materialTemplateVo);
    }
    @CrossOrigin
    @PostMapping("/material/updateMaterialTemplate")
    public String updateMaterialTemplate(@RequestBody MaterialTemplateVo materialTemplateVo) throws Exception{
        return templateService.updateMaterialTemplate(materialTemplateVo);
    }






    /*
    以下为设备模板的controller
     */

    @CrossOrigin
    @GetMapping("/equipment/all")
    public Object equipmentAll(String company_id) throws Exception {
        System.out.println(company_id);
        List<EquipmentTemplate> equipmentTemplates = templateService.getAllEquipment(company_id);
        //map的key为设备名，value为对应属性名称列表，如：{"切割机":["价格","功率"],"打卡机":["输入电压"]}
        HashMap<String,Object> map = new HashMap<>();
        for(EquipmentTemplate t : equipmentTemplates) {
            List<String> lst;
            if(map.containsKey(t.getName())){
                lst = (List<String>) map.get(t.getName());
            }else{
                lst = new LinkedList<>();
            }
            lst.add(t.getAttribute());
            map.put(t.getName(),lst);
        }
        for(String a : map.keySet()){
            System.out.println(a + map.get(a));
        }
        return map;
    }

    @CrossOrigin
    @GetMapping("/equipment/getEquipmentTemplateByName")  //全部物料模板信息 格式在下方注释
    public Object getEquipmentTemplateByName(String name,String company_id) throws Exception {
        List<EquipmentTemplate> equipmentTemplates = templateService.getEquipmentTemplateByName(name,company_id);
        //map的key为设备名，value为对应属性名称列表，如：{"切割机":["价格","功率"],"打卡机":["输入电压"]}
        HashMap<String,Object> map = new HashMap<>();
        for(EquipmentTemplate t : equipmentTemplates) {
            List<String> lst;
            if(map.containsKey(t.getName())){
                lst = (List<String>) map.get(t.getName());
            }else{
                lst = new LinkedList<>();
            }
            lst.add(t.getAttribute());
            map.put(t.getName(),lst);
        }
        return map;
    }

    @CrossOrigin
    @PostMapping("/equipment/addEquipmentTemplate")
    public String addEquipmentTemplate(@RequestBody EquipmentTemplateVO equipmentTemplateVO) throws Exception{
        return templateService.addEquipmentTemplate(equipmentTemplateVO);
    }

    @CrossOrigin
    @PostMapping("/equipment/deleteEquipmentTemplate")
    public String deleteEquipmentTemplate(@RequestBody EquipmentTemplateVO equipmentTemplateVO) throws Exception{
        return templateService.deleteEquipmentTemplate(equipmentTemplateVO);
    }

    @CrossOrigin
    @PostMapping("/equipment/updateEquipmentTemplate")
    public String updateEquipmentTemplate(@RequestBody EquipmentTemplateVO equipmentTemplateVO) throws Exception{
        return templateService.updateEquipmentTemplate(equipmentTemplateVO);
    }



}
