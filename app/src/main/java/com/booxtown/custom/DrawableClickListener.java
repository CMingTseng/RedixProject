package com.booxtown.custom;

/**
 * Created by Administrator on 12/02/2017.
 */

public interface DrawableClickListener {
    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}
