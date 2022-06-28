package com.example.mes.template.service;

import com.example.mes.process.Vo.MaterialVo.DeleteMaterialVo;
import com.example.mes.process.Vo.MaterialVo.UpdateMaterialVo;

import com.example.mes.process.Vo.OptionsVo.OptionMaterial;
import com.example.mes.template.entity.EquipmentTemplate;
import com.example.mes.template.entity.MaterialTemplate;
import com.example.mes.template.mapper.TemplateMapper;
import com.example.mes.template.vo.EquipmentTemplateVO;
import com.example.mes.template.vo.MaterialTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class TemplateService implements TemplateServiceImpl{

    @Autowired
    private TemplateMapper templateMapper;

    public List<MaterialTemplate> getAllMaterial(String company_id) {
        return templateMapper.getInfo(company_id);
    }

    @Override
    public List<MaterialTemplate> getMaterialTemplateByName(String name,String company_id) {
        return templateMapper.selectMaterialTemplateByName(name,company_id);
    }

    @Override
    public List<MaterialTemplate> getMaterialTemplateByID(int material_id,String company_id) {
        return templateMapper.selectMaterialTemplateById(material_id,company_id);
    }

    //增加
    @Transactional
    @Override
    public String addMaterialTemplate(MaterialTemplateVo materialTemplateVo) {
        try {

            for(String attribute : materialTemplateVo.getAttribute()){

                MaterialTemplate materialTemplate = new MaterialTemplate();
                materialTemplate.setMaterial_id(materialTemplateVo.getMaterial_id());
                materialTemplate.setCompany_id(materialTemplateVo.getCompany_id());
                materialTemplate.setAttribute(attribute);
                materialTemplate.setName(materialTemplateVo.getName());
//                System.out.println(materialTemplate.getMaterial_id());
//                System.out.println(materialTemplate.getName());
                templateMapper.addMaterialTemplate(materialTemplate);
            }
            return "yes";

            }catch (Exception e){
            e.printStackTrace();

            return "no";
        }

    }

    //删除
    @Override
    public String deleteMaterialTemplate(MaterialTemplateVo materialTemplateVo) {
        try {

                templateMapper.deleteMaterialTemplateByName(materialTemplateVo.getName(),Integer.toString(materialTemplateVo.getCompany_id()));

            return "yes";

        }catch (Exception e){
            e.printStackTrace();

            return "no";
        }

    }

    //更新模板
    @Transactional
    @Override
    public String updateMaterialTemplate(MaterialTemplateVo materialTemplateVo) {
        try {

            templateMapper.deleteMaterialTemplateByName(materialTemplateVo.getName(),Integer.toString(materialTemplateVo.getCompany_id()));


            for(String attribute : materialTemplateVo.getAttribute()){

                MaterialTemplate materialTemplate = new MaterialTemplate();
                materialTemplate.setMaterial_id(materialTemplateVo.getMaterial_id());
                materialTemplate.setCompany_id(materialTemplateVo.getCompany_id());
                materialTemplate.setAttribute(attribute);
                materialTemplate.setName(materialTemplateVo.getName());
                templateMapper.addMaterialTemplate(materialTemplate);
            }
            return "yes";

        }catch (Exception e){
            e.printStackTrace();

            return "no";
        }
    }


    /*
    以下为设备模板的service
     */
    @Override
    public List<EquipmentTemplate> getAllEquipment(String company_id) {
        return templateMapper.getEquipmentInfo(company_id);
    }

    @Override
    public List<EquipmentTemplate> getEquipmentTemplateByName(String name,String company_id) {
        return templateMapper.selectEquipmentTemplateByName(name, company_id);
    }

    @Override
    public List<EquipmentTemplate> getEquipmentTemplateByID(int equipment_id,String company_id) {
        return templateMapper.selectEquipmentTemplateById(equipment_id,company_id);
    }

    @Transactional
    @Override
    public String addEquipmentTemplate(EquipmentTemplateVO equipmentTemplateVO){
        try {

            templateMapper.deleteEquipmentTemplateByName(equipmentTemplateVO.getName(),Integer.toString(equipmentTemplateVO.getCompany_id()));


            for(String attribute : equipmentTemplateVO.getAttribute()){

                EquipmentTemplate equipmentTemplate = new EquipmentTemplate();
                equipmentTemplate.setEquipment_id(equipmentTemplateVO.getEquipment_id());
                equipmentTemplate.setCompany_id(equipmentTemplateVO.getCompany_id());
                equipmentTemplate.setAttribute(attribute);
                equipmentTemplate.setName(equipmentTemplateVO.getName());
                templateMapper.addEquipmentTemplate(equipmentTemplate);
            }
            return "yes";

        }catch (Exception e){
            e.printStackTrace();

            return "no";
        }
    }

    @Override
    public String deleteEquipmentTemplate(EquipmentTemplateVO equipmentTemplateVO) {
        try {

            templateMapper.deleteEquipmentTemplateByName(equipmentTemplateVO.getName(),Integer.toString(equipmentTemplateVO.getCompany_id()));

            return "yes";

        }catch (Exception e){
            e.printStackTrace();

            return "no";
        }

    }

    @Transactional
    @Override
    public String updateEquipmentTemplate(EquipmentTemplateVO equipmentTemplateVO) {
        try {

            templateMapper.deleteEquipmentTemplateByName(equipmentTemplateVO.getName(),Integer.toString(equipmentTemplateVO.getCompany_id()));


            for(String attribute : equipmentTemplateVO.getAttribute()){

                EquipmentTemplate equipmentTemplate = new EquipmentTemplate();
                equipmentTemplate.setEquipment_id(equipmentTemplateVO.getEquipment_id());
                equipmentTemplate.setCompany_id(equipmentTemplateVO.getCompany_id());
                equipmentTemplate.setAttribute(attribute);
                equipmentTemplate.setName(equipmentTemplateVO.getName());
                templateMapper.addEquipmentTemplate(equipmentTemplate);
            }
            return "yes";

        }catch (Exception e){
            e.printStackTrace();

            return "no";
        }
    }





}
