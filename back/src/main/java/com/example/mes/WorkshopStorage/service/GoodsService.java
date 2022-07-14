package com.example.mes.WorkshopStorage.service;

import com.example.mes.WorkshopStorage.mapper.*;
import com.example.mes.WorkshopStorage.para.GoodsUpdatePara;
import com.example.mes.WorkshopStorage.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsMessageMapper goodsMessageMapper;
    @Autowired
    private ShelfMapper shelfMapper;
    @Autowired
    private WorkshopMapper workshopMapper;
    @Autowired
    private StorageMapper storageMapper;
    public void setTime(ApplyVo workshop, String user) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        workshop.setUuid(uuid);
        workshop.setStatus("0");
        workshop.setIs_deleted("0");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        workshop.setCreated_by(user);
        workshop.setModified_by(user);
        workshop.setCreated_time(timestamp);
        workshop.setModified_time(timestamp);
    }
    public void setTime(GoodsVo workshop, String user) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        workshop.setUuid(uuid);
        workshop.setStatus("0");
        workshop.setIs_deleted("0");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        workshop.setCreated_by(user);
        workshop.setModified_by(user);
        workshop.setCreated_time(timestamp);
        workshop.setModified_time(timestamp);
    }

    public void setTime(GoodsMessageVo workshop, String user) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        workshop.setUuid(uuid);
        workshop.setStatus("0");
        workshop.setIs_deleted("0");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        workshop.setCreated_by(user);
        workshop.setModified_by(user);
        workshop.setCreated_time(timestamp);
        workshop.setModified_time(timestamp);
    }

    private Timestamp getTime(){
        return new Timestamp(System.currentTimeMillis());
    }
    public Result<List<GoodsVo>> search(String storage_id, String id){
        Result<List<GoodsVo>> result = new Result<>();
        List<GoodsVo> goodsVoList = goodsMapper.getByShelfId(storage_id, id);
        result.setResult(goodsVoList);
        return result;
    }

    public void move(GoodsUpdatePara params) throws SQLException {

        Timestamp timestamp = getTime();
        GoodsVo goodsVo = goodsMapper.getById(params.getStorage_id(), params.getShelf_id(), params.getLocation());
        GoodsVo goodsVo1 = goodsMapper.getById(params.getNewStorage_id(), params.getNewShelf_id(), params.getNewLocation());
        if(goodsVo1 != null) {
            if(!goodsVo1.getGoods_id().equals(goodsVo.getGoods_id()) || !goodsVo1.getTypes().equals(goodsVo.getTypes())){
                throw new SQLException();
            }
        }
        if(goodsVo1 != null){
            goodsMapper.updateQuantity(goodsVo1.getStorage_id(), goodsVo1.getShelf_id(), goodsVo1.getId(), goodsVo1.getQuantity() + goodsVo.getQuantity(), timestamp);
        }else{
            goodsVo.setStorage_id(params.getNewStorage_id());
            goodsVo.setShelf_id(params.getNewShelf_id());
            goodsVo.setId(params.getNewLocation());
            goodsVo.setModified_time(timestamp);
            goodsVo.setCreated_time(timestamp);
            goodsMapper.insert(goodsVo);
        }
        goodsMapper.delete(params.getStorage_id(), params.getShelf_id(), params.getLocation(), timestamp);
        List<GoodsMessageVo> goodsMessageVoList = goodsMessageMapper.getById(params.getStorage_id(), params.getShelf_id(), params.getLocation());

        for(GoodsMessageVo goodsMessageVo : goodsMessageVoList){
            goodsMessageVo.setCreated_time(timestamp);
            goodsMessageVo.setModified_time(timestamp);
            goodsMessageVo.setStorage_id(goodsVo.getStorage_id());
            goodsMessageVo.setShelf_id(goodsVo.getShelf_id());
            goodsMessageVo.setLocation(goodsVo.getId());
            goodsMessageMapper.delete(goodsMessageVo.getUuid(), timestamp);
            goodsMessageMapper.insert(goodsMessageVo);
        }
    }

    public void apply(String workshop_id, String id, int quantity, String type, String user, String in_out) throws SQLException {
        if(workshopMapper.getById(workshop_id) == null){
            throw new SQLException();
        }
        String name;
        if(type.equals("material")){
            name = goodsMapper.getMaterial(id);
            if(name == null){
                throw new SQLException();
            }
        }else{
            name = goodsMapper.getEquipment(id);
            if(name == null){
                throw new SQLException();
            }
        }
        ApplyVo applyVo = new ApplyVo();
        setTime(applyVo, user);
        applyVo.setGoods_name(name);
        applyVo.setWorkshop_id(workshop_id);
        applyVo.setGoods_id(id);
        applyVo.setPermit("0");
        applyVo.setQuantity(quantity);
        applyVo.setTypes(type);
        applyVo.setIn_out(in_out);
        goodsMapper.addApply(applyVo);
    }

    public Result<PageVo<ApplyVo>> applyItem(String page, String page_size) throws SQLException {
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getApplyCount();
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages());
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public Result<PageVo<ApplyVo>> searchApply(String page, String page_size, String goods_id, String goods_name, Date time1, Date time2, String type) throws SQLException {
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getApplyCount_search(goods_id, goods_name, time1, time2, type);
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectAll_search((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages(), goods_id, goods_name, time1, time2, type);
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public Result<Integer> searchQuantity(String goods_id, String types){
        Result<Integer> result = new Result<>();
        Integer count = goodsMapper.searchQuantity(goods_id, types);
        if(count == null)
            count = 0;
        result.setResult(count);
        return result;
    }

    public void setApply(String workshop_id, String user) throws SQLException {
        ApplyVo applyVo = goodsMapper.getByUuid(workshop_id);
        Integer count = goodsMapper.searchQuantity(applyVo.getGoods_id(), applyVo.getTypes());
        if(count == null)
            count = 0;
        if(count < applyVo.getQuantity()){
            throw new SQLException();
        }
        goodsMapper.setApply(workshop_id, user, getTime());
    }

    public Result<PageVo<ApplyVo>> setApplyItem(String page, String page_size) throws SQLException{
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getsetApplyCount();
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectsetAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages());
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public void managerConfirm(String uuid, String message, String user){
        if(message.equals("1")){
            message = "2";
        }else{
            message = "10";
        }
        goodsMapper.managerConfirm(uuid, message, user, getTime());
    }

    public Result<PageVo<ApplyVo>> confirmItem(String page, String page_size) throws SQLException {
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getConfirmCount();
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectConfirmAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages());
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }






    public Result<PageVo<ApplyVo>> messageItem(String page, String page_size) throws SQLException {
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getMessageCount();
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectMessageAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages());
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public void remove(String uuid, String user){
         ApplyVo applyVo = goodsMapper.getByUuid(uuid);
         Timestamp timestamp = getTime();

         List<GoodsMessageVo> goodsMessageVoList = goodsMessageMapper.getOrderByGoods(applyVo.getGoods_id(), applyVo.getTypes());
         int cnt = applyVo.getQuantity();
         for(GoodsMessageVo goodsMessageVo : goodsMessageVoList){
             if(goodsMessageVo.getQuantity() <= cnt){
                 cnt -= goodsMessageVo.getQuantity();
                 goodsMessageMapper.removeByTime(applyVo.getGoods_id(), goodsMessageVo.getCreated_time(), timestamp, user);
                 goodsMapper.removeById(goodsMessageVo.getStorage_id(), goodsMessageVo.getShelf_id(), goodsMessageVo.getLocation(), 0, timestamp, user);
             }else{
                 goodsMapper.removeById(goodsMessageVo.getStorage_id(), goodsMessageVo.getShelf_id(), goodsMessageVo.getLocation(), goodsMessageVo.getQuantity() - cnt, timestamp, user);
                 List<GoodsMessageVo> goodsMessageVoList1 = goodsMessageMapper.getByTime(goodsMessageVo.getCreated_time());
                 for(GoodsMessageVo goodsMessageVo1 : goodsMessageVoList1){
                     cnt--;
                     goodsMessageMapper.delete(goodsMessageVo1.getUuid(), goodsMessageVo.getCreated_time());
                     if(cnt == 0)break;
                 }
             }
         }
         goodsMapper.removeApply(uuid, user, timestamp);
         goodsMapper.checkDelete();
    }

    public void addGoods(String storage_id, String shelf_id, String location, String id, int quantity, String type, String user) throws SQLException {
        String name;
        if(type.equals("material")){
            name = goodsMapper.getMaterial(id);
            if(name == null)
                throw new SQLException();
        }else{
            name = goodsMapper.getEquipment(id);
            if(name == null)
                throw new SQLException();
        }
        GoodsVo goodsVo = goodsMapper.getById(storage_id, shelf_id, location);
        if(goodsVo != null){
            if(!goodsVo.getTypes().equals(type) || !goodsVo.getGoods_id().equals(id)){
                throw new SQLException();
            }
        }
        if(storageMapper.search(storage_id) == null){
            throw new SQLException();
        }
        if(shelfMapper.getById(storage_id, shelf_id) == null){
            throw new SQLException();
        }
        ApplyVo applyVo = new ApplyVo();
        setTime(applyVo, user);
        applyVo.setStorage_id(storage_id);
        applyVo.setShelf_id(shelf_id);
        applyVo.setLocation(location);
        applyVo.setGoods_id(id);
        applyVo.setGoods_name(name);
        applyVo.setTypes(type);
        applyVo.setQuantity(quantity);
        applyVo.setPermit("100");
        goodsMapper.addApply(applyVo);
    }

    public Result<PageVo<ApplyVo>> addItem(String page, String page_size) throws SQLException {
        Result<PageVo<ApplyVo>> result = new Result<>();
        PageVo<ApplyVo> pageVo = new PageVo();
        Integer size = goodsMapper.getAddCount();
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<ApplyVo> currentlist = goodsMapper.selectAddAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages());
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public void addConfirm(String uuid, String message, String user) throws SQLException {
        Timestamp timestamp = getTime();
        goodsMapper.managerConfirm(uuid, "50", user, timestamp);
        if(message.equals("0")){
            return ;
        }
        ApplyVo applyVo = goodsMapper.getByUuid(uuid);
        if(storageMapper.search(applyVo.getStorage_id()) == null){
            throw new SQLException();
        }
        if(shelfMapper.getById(applyVo.getStorage_id(), applyVo.getShelf_id()) == null){
            throw new SQLException();
        }

        GoodsVo goodsVo = goodsMapper.getById(applyVo.getStorage_id(), applyVo.getShelf_id(), applyVo.getLocation());
        if(goodsVo != null){
            if(!goodsVo.getGoods_id().equals(applyVo.getGoods_id()) || !goodsVo.getTypes().equals(applyVo.getTypes())){
                throw new SQLException();
            }
        }
        if(goodsVo != null){
            goodsMapper.updateQuantity(applyVo.getStorage_id(), applyVo.getShelf_id(), applyVo.getLocation(), applyVo.getQuantity() + goodsVo.getQuantity(), timestamp);
        }else{
            GoodsVo goodsVo1 = new GoodsVo();
            setTime(goodsVo1, user);
            goodsVo1.setId(applyVo.getLocation());
            goodsVo1.setStorage_id(applyVo.getStorage_id());
            goodsVo1.setShelf_id(applyVo.getShelf_id());
            goodsVo1.setGoods_id(applyVo.getGoods_id());
            goodsVo1.setGoods_name(applyVo.getGoods_name());
            goodsVo1.setQuantity(applyVo.getQuantity());
            goodsVo1.setVerify("normal");
            goodsVo1.setTypes(applyVo.getTypes());
            goodsMapper.insert(goodsVo1);

        }
        for(int i = 0; i < applyVo.getQuantity(); i++){
            GoodsMessageVo goodsMessageVo = new GoodsMessageVo();
            setTime(goodsMessageVo, user);
            goodsMessageVo.setId(applyVo.getGoods_id());
            goodsMessageVo.setStorage_id(applyVo.getStorage_id());
            goodsMessageVo.setShelf_id(applyVo.getShelf_id());
            goodsMessageVo.setLocation(applyVo.getLocation());
            goodsMessageVo.setTypes(applyVo.getTypes());
            goodsMessageVo.setIor("insert");
            goodsMessageVo.setCreated_time(timestamp);
            goodsMessageVo.setModified_time(timestamp);
            goodsMessageMapper.insert(goodsMessageVo);
        }
    }

    public Result<PageVo<GoodsVo>> goodsItem(String page, String page_size, String storage_id, String shelf_id) throws SQLException {
        Result<PageVo<GoodsVo>> result = new Result<>();
        PageVo<GoodsVo> pageVo = new PageVo();
        Integer size = goodsMapper.getGoodsCount(storage_id, shelf_id);
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<GoodsVo> currentlist = goodsMapper.selectGoodsAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages(), storage_id, shelf_id);
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }

    public Result<PageVo<GoodsVo>> locationItem(String page, String page_size, String storage_id, String shelf_id) throws SQLException {
        Result<PageVo<GoodsVo>> result = new Result<>();
        PageVo<GoodsVo> pageVo = new PageVo();
        Integer size = goodsMapper.getLocationCount(storage_id, shelf_id);
        if(size == null)
            size = 0;
        pageVo.setTotal(size);
        pageVo.setSize((size - 1) / Integer.parseInt(page_size) + 1);
        if (pageVo.getSize() < 0) {
            pageVo.setSize(0);
        }
        if (Integer.parseInt(page) > pageVo.getSize()) {
            throw new SQLException();
        }
        if (Integer.parseInt(page) == pageVo.getSize()) {
            pageVo.setPages((size - 1) % Integer.parseInt(page_size) + 1);
        } else {
            pageVo.setPages(Integer.parseInt(page_size));
        }
        pageVo.setCurrent(Integer.parseInt(page));
        List<GoodsVo> currentlist = goodsMapper.selectLocationAll((pageVo.getCurrent() - 1) * Integer.parseInt(page_size), pageVo.getPages(), storage_id, shelf_id);
        pageVo.setRecords(currentlist);
        result.setResult(pageVo);
        return result;
    }
}
