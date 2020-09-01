package com.github.saleson.fm.component;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author saleson
 * @date 2020-03-20 10:54
 */
@Getter
public class DataPage<T> {
    private List<T> items;
    private int pagesize;

    public DataPage(List<T> items, int pagesize) {
        this.items = items;
        this.pagesize = pagesize;
    }

    /**
     *
     * @param page begin by zero.
     * @return
     */
    public List<T> getPageItems(int page){
        int start = page * pagesize;
        if(start>=items.size()){
            throw new IndexOutOfBoundsException("page is too large");
        }
        int len = Math.min(pagesize, items.size() - start);
        List<T> itemList = new ArrayList<>(len);
        int end = start + len;
        for(int i=start;i<end;i++){
            itemList.add(items.get(i));
        }
        return itemList;
    }

    public int getPage(){
        return (int ) Math.ceil(items.size() * 1.0 / pagesize);
    }


    public void pageForEach(Consumer<List<T>> consumer){
        int totalPage = getPage();
        for(int i =0;i<totalPage;i++){
            consumer.accept(getPageItems(i));
        }
    }

}
