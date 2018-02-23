package com.gtafe.data.center.common.common.vo;


import java.util.Iterator;
import java.util.List;


public abstract class RemoveEmptyChildVO {
    /**
     * 是否叶子节点
     */
    public abstract boolean gtLeaf();
    /**
     * 获取孩子列表
     * @author 汪逢建
     * @date 2017年11月1日
     */
    public abstract List<? extends RemoveEmptyChildVO> gtChildren();

    /**
     * 移除空的非叶子节点,返回true表示需要移除，false不需要移除
     * @author 汪逢建
     * @date 2017年11月1日
     */
    public boolean removeEmptys() {
        if (this.gtLeaf()) {
            return false;
        }
        if (gtChildren() == null || gtChildren().isEmpty()) {
            return true;
        }
        boolean noChildLeaf = true;
        Iterator<? extends RemoveEmptyChildVO> it = this.gtChildren().iterator();
        while(it.hasNext()) {
            RemoveEmptyChildVO child = it.next();
            if(child.removeEmptys()) {
                it.remove();
            }else {
                noChildLeaf = false;
            }
        }
        return noChildLeaf;
    }
}
