package materialshop.curiousbat.shopmylist.model;

import android.graphics.drawable.Drawable;

/**
 * Navigation Drawer Class
 * Created by Zied on 17/10/2015.
 */
public class NavDrawerItem {

    private boolean showNotify;
    private String title;
    private Drawable iconId;

    public NavDrawerItem() {

    }

    public void setIconId(Drawable iconId) {
        this.iconId = iconId;
    }

    public Drawable getIconId() {
        return iconId;
    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
