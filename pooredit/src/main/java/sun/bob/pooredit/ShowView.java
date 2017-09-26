package sun.bob.pooredit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import sun.bob.pooredit.utils.Constants;
import sun.bob.pooredit.views.BaseContainer;
import sun.bob.pooredit.views.EditView;
import sun.bob.pooredit.views.File;
import sun.bob.pooredit.views.Image;

/**
 * Created by bob.sun on 15/11/26.
 */

public class ShowView extends LinearLayout {

    PoorEditWidget poorEditWidget;
    public static BaseContainer picking;
    public static Image.ImageLoaderItf imageLoaderItf = null;
    public ShowView(Context context) {
        super(context);
        initUI();
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI(){
        this.setOrientation(VERTICAL);
        poorEditWidget = new PoorEditWidget(getContext());
        this.addView(poorEditWidget);
//        this.addView(new ToolBar(getContext()));
        this.setBackgroundColor(Color.WHITE);
    }

    public String exportJSON(String where, String appearanceName){
        return poorEditWidget.exportJSON(where, appearanceName);
    }

    public void loadJson(String where, String appearanceName){
        poorEditWidget.loadJson(where, appearanceName);
    }

    public void setImageLoader(Image.ImageLoaderItf imageLoaderItf) {
        this.imageLoaderItf = imageLoaderItf;
    }

    class PoorEditWidget extends ScrollView {
        EditView editView;
        public PoorEditWidget(Context context) {
            super(context);
            initUI();
        }

        public PoorEditWidget(Context context, AttributeSet attrs) {
            super(context, attrs);
            initUI();
        }

        private void initUI(){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(layoutParams);
            editView = new EditView(getContext());
            editView.setEnabled(false);
            editView.requestFocus();
            this.addView(editView);
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        public String exportJSON(String where, String appearanceName){
            return editView.exportJSON(where, appearanceName);
        }

        public void loadJson(String where, String appearanceName){
            editView.loadJson(where, appearanceName);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (picking == null){
            return;
        }
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (data == null){
            return;
        }
        switch (requestCode){
            case Constants.REQ_PICK_IMAGE:
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picPath = cursor.getString(columnIndex);
                cursor.close();
                if (picPath == null || picPath.length() == 0){
                    break;
                }
                ((Image) picking).setImage(picPath, 0);

                break;
            case Constants.REQ_PICK_FILE:
                Uri selectedFile = data.getData();
                ((File) picking).setFilePath(selectedFile.getPath());
                break;
            default:
                break;
        }
    }
}

