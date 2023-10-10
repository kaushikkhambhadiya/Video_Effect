package com.capstone.videoeffect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.videoeffect.Adapters.FrameAdapter;
import com.capstone.videoeffect.Adapters.GlareAdapter;
import com.capstone.videoeffect.Adapters.StickerAdapter;
import com.capstone.videoeffect.NewAddText.CardColorAdapter;
import com.capstone.videoeffect.NewAddText.CardFontStyleAdapter;
import com.capstone.videoeffect.NewAddText.CardPatternAdapter;
import com.capstone.videoeffect.stickerview.StickerView;
import com.capstone.videoeffect.utils.Effects;
import com.capstone.videoeffect.utils.Glob;
import com.capstone.videoeffect.view.HorizontalListView;
import com.capstone.videoeffect.view.SquareImageView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;



public class ImageEditingActivity extends AppCompatActivity implements OnClickListener {
    static int[] COLORS = new int[]{Color.parseColor("#b71c1c"), Color.parseColor("#c62828"), Color.parseColor("#d32f2f"), Color.parseColor("#e53935"), Color.parseColor("#f44336"), Color.parseColor("#ef5350"), Color.parseColor("#e57373"), Color.parseColor("#ef9a9a"), Color.parseColor("#ffcdd2"), Color.parseColor("#1b5e20"), Color.parseColor("#2e7d32"), Color.parseColor("#388e3c"), Color.parseColor("#43a047"), Color.parseColor("#4caf50"), Color.parseColor("#66bb6a"), Color.parseColor("#81c784"), Color.parseColor("#a5d6a7"), Color.parseColor("#c8e6c9"), Color.parseColor("#0d47a1"), Color.parseColor("#1565c0"), Color.parseColor("#1976d2"), Color.parseColor("#1e88e5"), Color.parseColor("#2196f3"), Color.parseColor("#42a5f5"), Color.parseColor("#64b5f6"), Color.parseColor("#90caf9"), Color.parseColor("#bbdefb"), Color.parseColor("#f57f17"), Color.parseColor("#f9a825"), Color.parseColor("#fbc02d"), Color.parseColor("#fdd835"), Color.parseColor("#ffeb3b"), Color.parseColor("#ffee58"), Color.parseColor("#fff176"), Color.parseColor("#fff59d"), Color.parseColor("#fff9c4"), Color.parseColor("#e65100"), Color.parseColor("#ef6c00"), Color.parseColor("#f57c00"), Color.parseColor("#fb8c00"), Color.parseColor("#ff9800"), Color.parseColor("#ffa726"), Color.parseColor("#ffb74d"), Color.parseColor("#ffcc80"), Color.parseColor("#ffe0b2"), Color.parseColor("#880e4f"), Color.parseColor("#ad1457"), Color.parseColor("#c2185b"), Color.parseColor("#d81b60"), Color.parseColor("#e91e63"), Color.parseColor("#ec407a"), Color.parseColor("#f06292"), Color.parseColor("#f48fb1"), Color.parseColor("#f8bbd0"), Color.parseColor("#4a148c"), Color.parseColor("#6a1b9a"), Color.parseColor("#7b1fa2"), Color.parseColor("#8e24aa"), Color.parseColor("#9c27b0"), Color.parseColor("#ab47bc"), Color.parseColor("#ba68c8"), Color.parseColor("#ce93d8"), Color.parseColor("#e1bee7"), Color.parseColor("#3e2723"), Color.parseColor("#4e342e"), Color.parseColor("#5d4037"), Color.parseColor("#6d4c41"), Color.parseColor("#795548"), Color.parseColor("#8d6e63"), Color.parseColor("#a1887f"), Color.parseColor("#bcaaa4"), Color.parseColor("#d7ccc8"), Color.parseColor("#212121"), Color.parseColor("#424242"), Color.parseColor("#616161"), Color.parseColor("#757575"), Color.parseColor("#9e9e9e"), Color.parseColor("#bdbdbd"), Color.parseColor("#e0e0e0"), Color.parseColor("#eeeeee"), Color.parseColor("#f5f5f5"), Color.parseColor("#006064"), Color.parseColor("#00838f"), Color.parseColor("#0097a7"), Color.parseColor("#00acc1"), Color.parseColor("#00bcd4"), Color.parseColor("#26c6da"), Color.parseColor("#4dd0e1"), Color.parseColor("#80deea"), Color.parseColor("#b2ebf2")};
    private static final int FINAL_SAVE = 3;
    private static final int MY_REQUEST_CODE = 2;
    private static final int MY_REQUEST_CODE2 = 5;
    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;
    private static Bitmap f1582b;
    private static Canvas f1583c;
    public static Bitmap finalEditedImage;
    public static Uri selectedUri;
    public static String urlForShareImage;
    private ImageView ImgFrm;
    private SquareImageView Img_light;
    private ImageView back;
    ImageView btn_addtext_done;
    TextView btn_alignment_done;
    TextView btn_color_done;
    TextView btn_fontstyle_done;
    TextView btn_pattern_done;
    TextView btn_text_done;
    ImageView btnsave;
    final Context context = this;
    private int cornerRadious = 10;
    float dx = 0.0f;
    float dy = 0.0f;
    EditText editAddText;
    private Bitmap editedBitmap;
    private ImageView ef1;
    private ImageView ef10;
    private ImageView ef11;
    private ImageView ef12;
    private ImageView ef13;
    private ImageView ef14;
    private ImageView ef15;
    private ImageView ef16;
    private ImageView ef17;
    private ImageView ef18;
    private ImageView ef19;
    private ImageView ef2;
    private ImageView ef20;
    private ImageView ef21;
    private ImageView ef22;
    private ImageView ef3;
    private ImageView ef4;
    private ImageView ef5;
    private ImageView ef6;
    private ImageView ef7;
    private ImageView ef8;
    private ImageView ef9;
    private ImageView ef_original;
    private FrameLayout fl_Sticker;
    private boolean flagForFlip = true;
    String[] fonts = new String[]{"font1.ttf", "font2.ttf", "font3.ttf", "font4.TTF", "font5.ttf", "font6.TTF", "font7.ttf", "font8.ttf", "font9.ttf", "font10.TTF", "font11.ttf", "font12.ttf", "font14.TTF", "font16.TTF", "font17.ttf", "font18.ttf", "font19.ttf", "font20.ttf", "font21.ttf"};
    private BaseAdapter frameAdapter;
    private ArrayList<Integer> frmList;
    FrameLayout frm_Main;
    GridView grid_color;
    GridView grid_fontstyle;
    GridView grid_pattern;
    private HorizontalListView hl_Frm;
    ImageView imgAlign;
    ImageView imgPattern;
    ImageView imgTextSize;
    ImageView imgcolor;
    ImageView imgstyle;
    ImageView imgtext;
    private int initColor;

    private boolean isLight = false;
    private ImageView ivFinalText;
    LinearLayout lin_add_text;
    LinearLayout lin_alignment;
    ImageView lin_back;
    LinearLayout lin_color;
    LinearLayout lin_pattern;
    LinearLayout lin_style;
    LinearLayout lin_text;
    LinearLayout lin_textSize;
    private LinearLayout ll3D;
    private LinearLayout llColor;
    private LinearLayout llFilter;
    private LinearLayout llFlip;
    private LinearLayout llLight;
    private LinearLayout llOverView;
    private LinearLayout llRotate;
    private LinearLayout llSticker;
    private LinearLayout llText;
    private LinearLayout ll_Effecs_Panel;
    private LinearLayout ll_detail;
    private LinearLayout ll_effect_list;
    private StickerView mCurrentView;
    Dialog mDialog;
    RadioGroup mRadioGroup;
    private ArrayList<View> mViews;
    TextView nav_text1;
    private ImageView orgImage;
    int[] pattern = new int[]{R.drawable.plain, R.drawable.pattern_01, R.drawable.pattern_02, R.drawable.pattern_03, R.drawable.pattern_04, R.drawable.pattern_05, R.drawable.pattern_06, R.drawable.pattern_07, R.drawable.pattern_08, R.drawable.pattern_09, R.drawable.pattern_10};
    float radious = 0.0f;
    RelativeLayout relAddText;
    RelativeLayout relAllDrawText;
    RelativeLayout relFontStyle;
    RelativeLayout relPattern;
    RelativeLayout relTextColor;
    RelativeLayout relTextSize;
    RelativeLayout relconAlign;
    private int rotate = 1;
    private ImageView save;
    private SeekBar seekTextSize = null;
    private StickerAdapter stickerAdapter;
    private int stickerId;
    private ArrayList<Integer> stickerlist;
    ArrayList<Integer> stickerviewId = new ArrayList();
    String text = "";
    int textColor = ViewCompat.MEASURED_STATE_MASK;
    int textSize = 0;
    int[] thumb_pattern = new int[]{R.drawable.ic_panel_none, R.drawable.thumb_pattern_01, R.drawable.thumb_pattern_02, R.drawable.thumb_pattern_03, R.drawable.thumb_pattern_04, R.drawable.thumb_pattern_05, R.drawable.thumb_pattern_06, R.drawable.thumb_pattern_07, R.drawable.thumb_pattern_08, R.drawable.thumb_pattern_09, R.drawable.thumb_pattern_10};
    TextView txt_main;
    Typeface type;
    private int view_id;

    RelativeLayout relativeLayout;
    ProgressDialog dialog;

    //Firebase Storage
    StorageReference strRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    class Abcd implements OnItemClickListener {
        Abcd() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d("myname", String.valueOf(position));
            if (ImageEditingActivity.this.isLight) {
                ImageEditingActivity.this.Img_light.setImageResource(frmList.get(position));
                return;
            }
            ImageEditingActivity.this.ImgFrm.setImageResource(frmList.get(position));
            ImageEditingActivity.this.ImgFrm.setColorFilter(ContextCompat.getColor(ImageEditingActivity.this.context, R.color.white));
        }
    }

    class Abcde implements DialogInterface.OnClickListener {
        Abcde() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    class C13324 implements ColorPickerClickListener {
        C13324() {
        }

        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
            ImageEditingActivity.this.initColor = selectedColor;
            ImageEditingActivity.this.ImgFrm.setColorFilter(selectedColor);
        }
    }

    class C13335 implements OnColorSelectedListener {
        C13335() {
        }

        public void onColorSelected(int selectedColor) {
        }
    }


    class C13368 implements OnClickListener {
        C13368() {
        }

        public void onClick(View view) {
            ImageEditingActivity.this.mDialog.dismiss();
        }
    }

    class C13379 implements OnClickListener {
        C13379() {
        }

        public void onClick(View v) {
            if (ImageEditingActivity.this.relAddText.getVisibility() == View.GONE) {
                ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relAddText.setVisibility(View.VISIBLE);
            }
            ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
            ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
            ImageEditingActivity.this.relAddText.setVisibility(View.GONE);
            ImageEditingActivity.closeInput(ImageEditingActivity.this.relAddText);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initColor = getResources().getColor(R.color.white);
        setContentView(R.layout.activity_image_editing);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        strRef = FirebaseStorage.getInstance().getReference(user.getUid()).child("Photos");

        this.mViews = new ArrayList();
        bindView();
        createDir();
        ll3D.callOnClick();

    }

    private void createDir() {
        Glob.createDirIfNotExists(Glob.Edit_Folder_name);
    }

    private void bindView() {
        this.back = (ImageView) findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.save = (ImageView) findViewById(R.id.save);
        this.save.setOnClickListener(this);
        this.ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        this.ll_detail.setVisibility(View.GONE);
        this.ll_effect_list = (LinearLayout) findViewById(R.id.ll_effect_list);
        this.ll_effect_list.setVisibility(View.GONE);
        this.llOverView = (LinearLayout) findViewById(R.id.ll_overview);
        this.llOverView.setOnClickListener(this);
        this.llText = (LinearLayout) findViewById(R.id.ll_text);
        this.llText.setOnClickListener(this);
        this.llColor = (LinearLayout) findViewById(R.id.ll_color);
        this.llColor.setOnClickListener(this);
        this.ll3D = (LinearLayout) findViewById(R.id.ll_three_d);
        this.ll3D.setOnClickListener(this);
        this.llLight = (LinearLayout) findViewById(R.id.ll_light);
        this.llLight.setOnClickListener(this);
        this.frm_Main = (FrameLayout) findViewById(R.id.frm_Main);
        this.llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        this.llFilter.setOnClickListener(this);
        this.llSticker = (LinearLayout) findViewById(R.id.ll_sticker);
        this.llSticker.setOnClickListener(this);
        this.llRotate = (LinearLayout) findViewById(R.id.ll_Rotate);
        this.llRotate.setOnClickListener(this);
        this.llFlip = (LinearLayout) findViewById(R.id.ll_Flip);
        this.llFlip.setOnClickListener(this);
        this.hl_Frm = (HorizontalListView) findViewById(R.id.hl_Frm);
        this.orgImage = (ImageView) findViewById(R.id.org_Img);
        this.orgImage.setImageBitmap(PhotoHomeActivity.bitmap);
        this.fl_Sticker = (FrameLayout) findViewById(R.id.fl_Sticker);
        this.ImgFrm = (ImageView) findViewById(R.id.Img_Frm);
        this.Img_light = (SquareImageView) findViewById(R.id.Img_light);
        this.Img_light.setWidthHeight(this.orgImage.getMeasuredWidth());
        this.ImgFrm.setImageResource(R.drawable.pixel_01);
        this.hl_Frm.setOnItemClickListener(new Abcd());
        setFrmList();

        bindEffectIcon();

    }

    void openDetail() {
        this.ll_detail.setVisibility(View.VISIBLE);
        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, this.llOverView.getY() + 70.0f, this.llOverView.getY());
        anim.setDuration(300);
        anim.setFillAfter(true);
        this.ll_detail.startAnimation(anim);
    }

    private void setFrmList() {
        setArraylistForFrm();
        this.frameAdapter = new FrameAdapter(this, this.frmList);
        this.hl_Frm.setAdapter(this.frameAdapter);
    }

    void FilterList() {
        this.hl_Frm.setVisibility(View.GONE);
        this.ll_effect_list.setVisibility(View.VISIBLE);
        openDetail();
    }

    void overViewList() {
        setFrmList();
        this.hl_Frm.setVisibility(View.VISIBLE);
        this.ll_effect_list.setVisibility(View.GONE);
        openDetail();
    }

    void overView3dList() {
        setArraylistFor3DFrm();
        this.frameAdapter = new FrameAdapter(this, this.frmList);
        this.hl_Frm.setAdapter(this.frameAdapter);
        this.hl_Frm.setVisibility(View.VISIBLE);
        this.ll_effect_list.setVisibility(View.GONE);
        openDetail();
    }

    void overViewLightList() {
        setArraylistForLight();
        Log.d("myname", String.valueOf(frmList.size()));
        this.frameAdapter = new GlareAdapter(this, this.frmList);
        this.hl_Frm.setAdapter(this.frameAdapter);
        this.hl_Frm.setVisibility(View.VISIBLE);
        this.ll_effect_list.setVisibility(View.GONE);
        openDetail();
    }

    private void setArraylistForFrm() {
        this.frmList = new ArrayList();
        this.frmList.add(R.drawable.pixel_2);
        this.frmList.add(R.drawable.pixel_3);
        this.frmList.add(R.drawable.pixel_01);
        this.frmList.add(R.drawable.pixel_02);
        this.frmList.add(Integer.valueOf(R.drawable.pixel_03));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_4));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_6));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_7));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_8));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_04));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_05));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_06));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_9));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_13));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_14));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_15));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_16));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_17));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_18));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_19));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_20));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_21));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_22));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_23));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_24));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_25));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_26));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_27));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_28));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_31));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_32));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_33));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_34));
    }

    private void setArraylistFor3DFrm() {
        this.frmList = new ArrayList();
        this.frmList.add(Integer.valueOf(R.drawable.bg_3d_1_black));
        this.frmList.add(Integer.valueOf(R.drawable.bg_3d_3_black));
        this.frmList.add(Integer.valueOf(R.drawable.bg_3d_4_black));
        this.frmList.add(Integer.valueOf(R.drawable.bg_3d_5_black));
        this.frmList.add(Integer.valueOf(R.drawable.bg_3d_6_black));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_mask_1));
        this.frmList.add(Integer.valueOf(R.drawable.pixel_mask_2));
        this.frmList.add(Integer.valueOf(R.drawable.splash_06));
        this.frmList.add(Integer.valueOf(R.drawable.splash_01));
        this.frmList.add(Integer.valueOf(R.drawable.splash_02));
        this.frmList.add(Integer.valueOf(R.drawable.splash_03));
        this.frmList.add(Integer.valueOf(R.drawable.splash_04));
        this.frmList.add(Integer.valueOf(R.drawable.splash_05));
    }

    private void setArraylistForLight() {
        this.frmList = new ArrayList();
        this.frmList.add(Integer.valueOf(R.drawable.flare_06));
        this.frmList.add(Integer.valueOf(R.drawable.flare_02));
        this.frmList.add(Integer.valueOf(R.drawable.flare_03));
        this.frmList.add(Integer.valueOf(R.drawable.flare_04));
        this.frmList.add(Integer.valueOf(R.drawable.flare_05));
        this.frmList.add(Integer.valueOf(R.drawable.flare_01));
    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (locationPermission != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 4);
        return false;
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage((CharSequence) message).setPositiveButton((CharSequence) "OK", okListener).setNegativeButton((CharSequence) "Cancel", okListener).create().show();
    }

    public void onClick(View v) {
        this.isLight = false;
        switch (v.getId()) {
            case R.id.back:
                finish();
                return;
            case R.id.save:
                dialog=new ProgressDialog(ImageEditingActivity.this);
                dialog.setTitle("Please Wait");
                dialog.show();
                if (this.mCurrentView != null) {
                    this.mCurrentView.setInEdit(false);
                }
                saveImage(getMainFrameBitmap());
                shareActivity();
                return;
            case R.id.frm_Main:
                if (this.mCurrentView != null) {
                    this.mCurrentView.setInEdit(false);
                    return;
                }
                return;
            case R.id.ef_original:
                Effects.applyEffectNone(this.orgImage);
                return;
            case R.id.ef1:
                Effects.applyEffect1(this.orgImage);
                return;
            case R.id.ef2:
                Effects.applyEffect2(this.orgImage);
                return;
            case R.id.ef3:
                Effects.applyEffect3(this.orgImage);
                return;
            case R.id.ef4:
                Effects.applyEffect4(this.orgImage);
                return;
            case R.id.ef5:
                Effects.applyEffect5(this.orgImage);
                return;
            case R.id.ef6:
                Effects.applyEffect6(this.orgImage);
                return;
            case R.id.ef7:
                Effects.applyEffect7(this.orgImage);
                return;
            case R.id.ef8:
                Effects.applyEffect8(this.orgImage);
                return;
            case R.id.ef9:
                Effects.applyEffect9(this.orgImage);
                return;
            case R.id.ef10:
                Effects.applyEffect10(this.orgImage);
                return;
            case R.id.ef11:
                Effects.applyEffect11(this.orgImage);
                return;
            case R.id.ef12:
                Effects.applyEffect12(this.orgImage);
                return;
            case R.id.ef13:
                Effects.applyEffect13(this.orgImage);
                return;
            case R.id.ef14:
                Effects.applyEffect14(this.orgImage);
                return;
            case R.id.ef15:
                Effects.applyEffect15(this.orgImage);
                return;
            case R.id.ef16:
                Effects.applyEffect16(this.orgImage);
                return;
            case R.id.ef17:
                Effects.applyEffect17(this.orgImage);
                return;
            case R.id.ef18:
                Effects.applyEffect18(this.orgImage);
                return;
            case R.id.ef19:
                Effects.applyEffect19(this.orgImage);
                return;
            case R.id.ef20:
                Effects.applyEffect20(this.orgImage);
                return;
            case R.id.ef21:
                Effects.applyEffect21(this.orgImage);
                return;
            case R.id.ef22:
                Effects.applyEffect22(this.orgImage);
                return;
            case R.id.ll_three_d:
                overView3dList();
                return;
            case R.id.ll_overview:
                overViewList();
                return;
            case R.id.ll_color:
                ColorPickerDialogBuilder.with(this).setTitle("Choose Background color").initialColor(this.initColor).wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).density(12).setOnColorSelectedListener(new C13335()).setPositiveButton((CharSequence) "ok", new C13324()).setNegativeButton((CharSequence) "cancel", new Abcde()).build().show();
                return;
            case R.id.ll_light:
                this.isLight = true;
                overViewLightList();
                return;
            case R.id.ll_filter:
                FilterList();
                return;
            case R.id.ll_text:
                AddText2();
                return;
            case R.id.ll_sticker:
                showStickerDialog();
                return;
            case R.id.ll_Rotate:
                if (this.rotate == 1) {
                    this.orgImage.setRotation(90.0f);
                    this.rotate = 2;
                    return;
                } else if (this.rotate == 2) {
                    this.orgImage.setRotation(Glob.HUE_CYAN);
                    this.rotate = 3;
                    return;
                } else if (this.rotate == 3) {
                    this.orgImage.setRotation(Glob.HUE_VIOLET);
                    this.rotate = 4;
                    return;
                } else if (this.rotate == 4) {
                    this.orgImage.setRotation(360.0f);
                    this.rotate = 1;
                    return;
                } else {
                    return;
                }
            case R.id.ll_Flip:
                if (this.flagForFlip) {
                    this.orgImage.setRotationY(Glob.HUE_CYAN);
                    this.flagForFlip = false;
                    return;
                }
                this.orgImage.setRotationY(360.0f);
                this.flagForFlip = true;
                return;
            default:
                return;
        }
    }

    private Bitmap getMainFrameBitmap() {
        this.frm_Main.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(this.frm_Main.getDrawingCache());
        if (VERSION.SDK_INT >= 19) {
            bitmap.setConfig(Config.ARGB_8888);
        }
        this.frm_Main.setDrawingCacheEnabled(false);
        finalEditedImage = bitmap;
        Bitmap bmp = bitmap;
        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();
        int largeX = imgWidth;
        int largeY = imgHeight;
        int left = imgWidth;
        int right = imgWidth;
        int top = imgHeight;
        int bottom = imgHeight;
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (bmp.getPixel(i, j) != 0) {
                    if (i - 0 < left) {
                        left = i - 0;
                    }
                    if (largeX - i < right) {
                        right = largeX - i;
                    }
                    if (j - 0 < top) {
                        top = j - 0;
                    }
                    if (largeY - j < bottom) {
                        bottom = largeY - j;
                    }
                }
            }
        }
        Log.d("Trimed bitmap", "left:" + left + " right:" + right + " top:" + top + " bottom:" + bottom);
        return Bitmap.createBitmap(bmp, left, top, (imgWidth - left) - right, (imgHeight - top) - bottom);
    }

    private void saveImage(Bitmap bitmap2) {
        Log.v("TAG", "saveImageInCache is called");
        Bitmap bitmap = bitmap2;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name);
        String FileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpeg";
        File file = new File(dir, FileName);
        file.renameTo(file);
        String _uri = "file://" + filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + FileName;
        urlForShareImage = filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + FileName;
        Log.d("cache uri=", _uri);
        try {
            OutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(_uri))));
            uploadData(Uri.parse(_uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadData(Uri photoUri) {
        Uri uri = Uri.parse("file://"+ photoUri);
        final StorageReference ref = strRef.child(uri.getLastPathSegment());
        ref.putFile(uri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        final Uri taskResult = urlTask.getResult();
                        final String downloadUrl = taskResult.toString();
                        Log.i("download_url",downloadUrl);
                    }
                });
    }

    private void shareActivity() {
        dialog.dismiss();
        startActivityForResult(new Intent(getApplicationContext(), ShareActivity.class), 3);

    }

    private void bindEffectIcon() {
        this.ef_original = (ImageView) findViewById(R.id.ef_original);
        this.ef_original.setOnClickListener(this);
        this.ef1 = (ImageView) findViewById(R.id.ef1);
        this.ef1.setOnClickListener(this);
        this.ef2 = (ImageView) findViewById(R.id.ef2);
        this.ef2.setOnClickListener(this);
        this.ef3 = (ImageView) findViewById(R.id.ef3);
        this.ef3.setOnClickListener(this);
        this.ef4 = (ImageView) findViewById(R.id.ef4);
        this.ef4.setOnClickListener(this);
        this.ef5 = (ImageView) findViewById(R.id.ef5);
        this.ef5.setOnClickListener(this);
        this.ef6 = (ImageView) findViewById(R.id.ef6);
        this.ef6.setOnClickListener(this);
        this.ef7 = (ImageView) findViewById(R.id.ef7);
        this.ef7.setOnClickListener(this);
        this.ef8 = (ImageView) findViewById(R.id.ef8);
        this.ef8.setOnClickListener(this);
        this.ef9 = (ImageView) findViewById(R.id.ef9);
        this.ef9.setOnClickListener(this);
        this.ef10 = (ImageView) findViewById(R.id.ef10);
        this.ef10.setOnClickListener(this);
        this.ef11 = (ImageView) findViewById(R.id.ef11);
        this.ef11.setOnClickListener(this);
        this.ef12 = (ImageView) findViewById(R.id.ef12);
        this.ef12.setOnClickListener(this);
        this.ef13 = (ImageView) findViewById(R.id.ef13);
        this.ef13.setOnClickListener(this);
        this.ef14 = (ImageView) findViewById(R.id.ef14);
        this.ef14.setOnClickListener(this);
        this.ef15 = (ImageView) findViewById(R.id.ef15);
        this.ef15.setOnClickListener(this);
        this.ef16 = (ImageView) findViewById(R.id.ef16);
        this.ef16.setOnClickListener(this);
        this.ef17 = (ImageView) findViewById(R.id.ef17);
        this.ef17.setOnClickListener(this);
        this.ef18 = (ImageView) findViewById(R.id.ef18);
        this.ef18.setOnClickListener(this);
        this.ef19 = (ImageView) findViewById(R.id.ef19);
        this.ef19.setOnClickListener(this);
        this.ef20 = (ImageView) findViewById(R.id.ef20);
        this.ef20.setOnClickListener(this);
        this.ef21 = (ImageView) findViewById(R.id.ef21);
        this.ef21.setOnClickListener(this);
        this.ef22 = (ImageView) findViewById(R.id.ef22);
        this.ef22.setOnClickListener(this);
        Effects.applyEffectNone(this.ef_original);
        Effects.applyEffect1(this.ef1);
        Effects.applyEffect2(this.ef2);
        Effects.applyEffect3(this.ef3);
        Effects.applyEffect4(this.ef4);
        Effects.applyEffect5(this.ef5);
        Effects.applyEffect6(this.ef6);
        Effects.applyEffect7(this.ef7);
        Effects.applyEffect8(this.ef8);
        Effects.applyEffect9(this.ef9);
        Effects.applyEffect10(this.ef10);
        Effects.applyEffect11(this.ef11);
        Effects.applyEffect12(this.ef12);
        Effects.applyEffect13(this.ef13);
        Effects.applyEffect14(this.ef14);
        Effects.applyEffect15(this.ef15);
        Effects.applyEffect16(this.ef16);
        Effects.applyEffect17(this.ef17);
        Effects.applyEffect18(this.ef18);
        Effects.applyEffect19(this.ef19);
        Effects.applyEffect20(this.ef20);
        Effects.applyEffect21(this.ef21);
        Effects.applyEffect22(this.ef22);
    }

    public static void closeInput(final View caller) {
        caller.postDelayed(new Runnable() {
            public void run() {
                ((InputMethodManager) caller.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(caller.getWindowToken(), 2);
            }
        }, 100);
    }

    private void addTextView(String srt, float radious, float dx, float dy) {
        this.txt_main.setText(srt);
        this.txt_main.setShadowLayer(radious, dx, dy, this.textColor);
    }

    private void applyBlurMaskFilter(TextView txt_main, Blur style) {
        BlurMaskFilter filter = new BlurMaskFilter(txt_main.getTextSize() / 10.0f, style);
        txt_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        txt_main.getPaint().setMaskFilter(filter);
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(-2, -2);
            f1582b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Config.ARGB_8888);
            f1583c = new Canvas(f1582b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(f1583c);
            return f1582b;
        }
        f1582b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
        f1583c = new Canvas(f1582b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(f1583c);
        return f1582b;
    }


    protected void AddText2() {
        this.mDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mDialog.getWindow().setSoftInputMode(32);
        this.mDialog.setContentView(R.layout.text_dialog);
        this.nav_text1 = (TextView) this.mDialog.findViewById(R.id.nav_text);
        this.txt_main = (TextView) this.mDialog.findViewById(R.id.txt_main);
        this.mRadioGroup = (RadioGroup) this.mDialog.findViewById(R.id.rg);
        this.imgtext = (ImageView) this.mDialog.findViewById(R.id.text);
        this.imgtext.setImageResource(R.drawable.btn_add_text_hover);
        this.btn_text_done = (TextView) this.mDialog.findViewById(R.id.btn_text_done);
        this.btn_alignment_done = (TextView) this.mDialog.findViewById(R.id.btn_alignment_done);
        this.btn_pattern_done = (TextView) this.mDialog.findViewById(R.id.btn_pattern_done);
        this.btn_fontstyle_done = (TextView) this.mDialog.findViewById(R.id.btn_fontstyle_done);
        this.btn_addtext_done = (ImageView) this.mDialog.findViewById(R.id.btn_addtext_done);
        this.btn_color_done = (TextView) this.mDialog.findViewById(R.id.btn_color_done);
        this.relAllDrawText = (RelativeLayout) this.mDialog.findViewById(R.id.relAllDrawText);
        this.relTextSize = (RelativeLayout) this.mDialog.findViewById(R.id.relTextSize);
        this.relconAlign = (RelativeLayout) this.mDialog.findViewById(R.id.relconAlign);
        this.relPattern = (RelativeLayout) this.mDialog.findViewById(R.id.relPattern);
        this.relFontStyle = (RelativeLayout) this.mDialog.findViewById(R.id.relFontStyle);
        this.relAddText = (RelativeLayout) this.mDialog.findViewById(R.id.relAddText);
        this.relTextColor = (RelativeLayout) this.mDialog.findViewById(R.id.relTextColor);
        this.lin_add_text = (LinearLayout) this.mDialog.findViewById(R.id.lin_add_text);
        this.lin_text = (LinearLayout) this.mDialog.findViewById(R.id.lin_text);
        this.lin_textSize = (LinearLayout) this.mDialog.findViewById(R.id.lin_textSize);
        this.lin_color = (LinearLayout) this.mDialog.findViewById(R.id.lin_color);
        this.lin_pattern = (LinearLayout) this.mDialog.findViewById(R.id.lin_pattern);
        this.lin_style = (LinearLayout) this.mDialog.findViewById(R.id.lin_style);
        this.lin_alignment = (LinearLayout) this.mDialog.findViewById(R.id.lin_alignment);
        this.lin_back = (ImageView) this.mDialog.findViewById(R.id.lin_back);
        this.btnsave = (ImageView) this.mDialog.findViewById(R.id.main_img_save);
        this.seekTextSize = (SeekBar) this.mDialog.findViewById(R.id.sekTextSize);
        this.imgcolor = (ImageView) this.mDialog.findViewById(R.id.color);
        this.imgstyle = (ImageView) this.mDialog.findViewById(R.id.style);
        this.imgPattern = (ImageView) this.mDialog.findViewById(R.id.pattern);
        this.imgAlign = (ImageView) this.mDialog.findViewById(R.id.imgAlignment);
        this.imgTextSize = (ImageView) this.mDialog.findViewById(R.id.textSize);
        this.grid_pattern = (GridView) this.mDialog.findViewById(R.id.grid_pattern);
        this.grid_fontstyle = (GridView) this.mDialog.findViewById(R.id.grid_fontstyle);
        this.grid_color = (GridView) this.mDialog.findViewById(R.id.grid_color);
        this.editAddText = (EditText) this.mDialog.findViewById(R.id.edt_text);
        this.lin_back.setOnClickListener(new C13368());
        this.relAddText.setOnClickListener(new C13379());
        this.relAddText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_out));
        this.relAddText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in));
        this.relAddText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in));
        this.relAddText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_out));
        this.lin_text.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                    if (ImageEditingActivity.this.relAddText.getVisibility() == View.GONE) {
                        ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                        ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                        ImageEditingActivity.this.relAddText.setVisibility(View.VISIBLE);
                    } else {
                        ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                        ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                        ImageEditingActivity.this.relAddText.setVisibility(View.GONE);
                        ImageEditingActivity.closeInput(ImageEditingActivity.this.imgtext);
                    }
                    ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text_hover);
                    ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color);
                    ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size);
                    ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern);
                    ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style);
                    ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment);
                    ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                    ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                    ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                    ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
                }
            }
        });
        final GestureDetector gestureDetector = new GestureDetector(new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }
        });
        this.txt_main.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        this.btn_text_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
            }
        });
        this.btn_alignment_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
            }
        });
        this.btn_pattern_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
            }
        });
        this.btn_fontstyle_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
            }
        });
        this.btn_color_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
            }
        });
        this.btn_addtext_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditingActivity.closeInput(ImageEditingActivity.this.btn_text_done);
                ImageEditingActivity.this.relAddText.setVisibility(View.GONE);
                ImageEditingActivity.this.btnsave.setVisibility(View.VISIBLE);
                ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                ImageEditingActivity.this.relAddText.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                if (ImageEditingActivity.this.editAddText.getText().toString().equals("")) {
                    Toast.makeText(ImageEditingActivity.this.context, "add text first", Toast.LENGTH_SHORT).show();
                } else {
                    ImageEditingActivity.this.text = ImageEditingActivity.this.editAddText.getText().toString();
                    ImageEditingActivity.this.addTextView(ImageEditingActivity.this.text, ImageEditingActivity.this.radious, ImageEditingActivity.this.dx, ImageEditingActivity.this.dy);
                }
                ImageEditingActivity.this.editAddText.setText("");
            }
        });
        this.lin_color.setOnClickListener(new OnClickListener() {

            class C13261 implements OnItemClickListener {
                C13261() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageEditingActivity.this.txt_main.getPaint().setShader(null);
                    ImageEditingActivity.this.txt_main.setTextColor(ImageEditingActivity.COLORS[i]);
                    ImageEditingActivity.this.addTextView(ImageEditingActivity.this.text, ImageEditingActivity.this.radious, ImageEditingActivity.this.dx, ImageEditingActivity.this.dy);
                }
            }

            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                }
                if (ImageEditingActivity.this.relTextColor.getVisibility() == View.GONE) {
                    ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relTextColor.setVisibility(View.VISIBLE);
                } else {
                    ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relTextColor.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
                }
                ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text);
                ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color_hover);
                ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size);
                ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern);
                ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style);
                ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment);
                ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
                ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                ImageEditingActivity.this.grid_color.setAdapter(new CardColorAdapter(ImageEditingActivity.this, ImageEditingActivity.COLORS));
                ImageEditingActivity.this.grid_color.setOnItemClickListener(new C13261());
            }
        });
        this.lin_style.setOnClickListener(new OnClickListener() {

            class C13281 implements OnItemClickListener {
                C13281() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageEditingActivity.this.type = Typeface.createFromAsset(ImageEditingActivity.this.getAssets(), ImageEditingActivity.this.fonts[i]);
                    ImageEditingActivity.this.txt_main.setTypeface(ImageEditingActivity.this.type);
                    ImageEditingActivity.this.addTextView(ImageEditingActivity.this.text, ImageEditingActivity.this.radious, ImageEditingActivity.this.dx, ImageEditingActivity.this.dy);
                }
            }

            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                }
                if (ImageEditingActivity.this.relFontStyle.getVisibility() == View.GONE) {
                    ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relFontStyle.setVisibility(View.VISIBLE);
                } else {
                    ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relFontStyle.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relFontStyle.setVisibility(View.VISIBLE);
                }
                ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text);
                ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color);
                ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size);
                ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern);
                ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style_hover);
                ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment);
                ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
                ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                ImageEditingActivity.this.grid_fontstyle.setAdapter(new CardFontStyleAdapter(ImageEditingActivity.this, ImageEditingActivity.this.fonts));
                ImageEditingActivity.this.grid_fontstyle.setOnItemClickListener(new C13281());
            }
        });
        this.lin_pattern.setOnClickListener(new OnClickListener() {

            class C13291 implements OnItemClickListener {
                C13291() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageEditingActivity.this.txt_main.getPaint().setShader(new BitmapShader(BitmapFactory.decodeResource(ImageEditingActivity.this.getResources(), ImageEditingActivity.this.pattern[i]), TileMode.REPEAT, TileMode.REPEAT));
                    ImageEditingActivity.this.addTextView(ImageEditingActivity.this.text, ImageEditingActivity.this.radious, ImageEditingActivity.this.dx, ImageEditingActivity.this.dy);
                }
            }

            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                }
                if (ImageEditingActivity.this.relPattern.getVisibility() == View.GONE) {
                    ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relPattern.setVisibility(View.VISIBLE);
                } else {
                    ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relPattern.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                }
                ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text);
                ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color);
                ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size);
                ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern_hover);
                ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style);
                ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment);
                ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
                ImageEditingActivity.this.grid_pattern.setAdapter(new CardPatternAdapter(ImageEditingActivity.this, ImageEditingActivity.this.thumb_pattern));
                ImageEditingActivity.this.grid_pattern.setOnItemClickListener(new C13291());
            }
        });
        this.lin_textSize.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                }
                if (ImageEditingActivity.this.relTextSize.getVisibility() == View.GONE) {
                    ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relTextSize.setVisibility(View.VISIBLE);
                } else {
                    ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relTextSize.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                }
                ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text);
                ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color);
                ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size_hover);
                ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern);
                ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style);
                ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment);
                ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
            }
        });
        this.lin_alignment.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equalsIgnoreCase("")) {
                }
                if (ImageEditingActivity.this.relconAlign.getVisibility() == View.GONE) {
                    ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relconAlign.setVisibility(View.VISIBLE);
                } else {
                    ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_in));
                    ImageEditingActivity.this.relconAlign.startAnimation(AnimationUtils.loadAnimation(ImageEditingActivity.this.getApplicationContext(), R.anim.push_up_out));
                    ImageEditingActivity.this.relconAlign.setVisibility(View.GONE);
                }
                ImageEditingActivity.this.imgtext.setImageResource(R.drawable.btn_add_text);
                ImageEditingActivity.this.imgcolor.setImageResource(R.drawable.btn_color);
                ImageEditingActivity.this.imgTextSize.setImageResource(R.drawable.btn_text_size);
                ImageEditingActivity.this.imgPattern.setImageResource(R.drawable.btn_pattern);
                ImageEditingActivity.this.imgstyle.setImageResource(R.drawable.btn_style);
                ImageEditingActivity.this.imgAlign.setImageResource(R.drawable.btn_alignment_hover);
                ImageEditingActivity.this.relTextSize.setVisibility(View.GONE);
                ImageEditingActivity.this.relPattern.setVisibility(View.GONE);
                ImageEditingActivity.this.relFontStyle.setVisibility(View.GONE);
                ImageEditingActivity.this.relTextColor.setVisibility(View.GONE);
            }
        });
        this.mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_none) {
                    ImageEditingActivity.this.txt_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    ImageEditingActivity.this.txt_main.getPaint().setMaskFilter(null);
                }
                if (i == R.id.rb_inner) {
                    ImageEditingActivity.this.applyBlurMaskFilter(ImageEditingActivity.this.txt_main, Blur.INNER);
                }
                if (i == R.id.rb_normal) {
                    ImageEditingActivity.this.applyBlurMaskFilter(ImageEditingActivity.this.txt_main, Blur.NORMAL);
                }
                if (i == R.id.rb_outer) {
                    ImageEditingActivity.this.applyBlurMaskFilter(ImageEditingActivity.this.txt_main, Blur.OUTER);
                }
                if (i == R.id.rb_solid) {
                    ImageEditingActivity.this.applyBlurMaskFilter(ImageEditingActivity.this.txt_main, Blur.SOLID);
                }
            }
        });
        this.seekTextSize.setProgress(10);
        this.seekTextSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ImageEditingActivity.this.textSize = progress + 30;
                ImageEditingActivity.this.txt_main.setTextSize((float) ImageEditingActivity.this.textSize);
            }
        });
        this.txt_main.setDrawingCacheEnabled(true);
        this.btnsave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImageEditingActivity.this.txt_main.getText().toString().equals("")) {
                    Toast.makeText(ImageEditingActivity.this.context, "add text first to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageView img = new ImageView(ImageEditingActivity.this.getApplicationContext());
                ImageEditingActivity.this.txt_main.buildDrawingCache();
                img.setImageBitmap(ImageEditingActivity.this.txt_main.getDrawingCache());
                img.setVisibility(View.GONE);
                Glob.txtBitmap = ImageEditingActivity.loadBitmapFromView(img);
                Glob.txtBitmap = ImageEditingActivity.this.CropBitmapTransparency(Glob.txtBitmap);
                ImageEditingActivity.this.addTextStickerView(Glob.txtBitmap);
                ImageEditingActivity.this.mDialog.dismiss();
            }
        });
        this.mDialog.show();
        this.mDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ImageEditingActivity.this.mDialog.dismiss();
                return true;
            }
        });
    }

    private void addStickerView(int id) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageResource(id);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                ImageEditingActivity.this.mViews.remove(stickerView);
                ImageEditingActivity.this.fl_Sticker.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                ImageEditingActivity.this.mCurrentView.setInEdit(false);
                ImageEditingActivity.this.mCurrentView = stickerView;
                ImageEditingActivity.this.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = ImageEditingActivity.this.mViews.indexOf(stickerView);
                if (position != ImageEditingActivity.this.mViews.size() - 1) {
                    ImageEditingActivity.this.mViews.add(ImageEditingActivity.this.mViews.size(), (StickerView) ImageEditingActivity.this.mViews.remove(position));
                }
            }
        });
        this.fl_Sticker.addView(stickerView, new LayoutParams(-1, -1));
        this.mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void addTextStickerView(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                ImageEditingActivity.this.mViews.remove(stickerView);
                ImageEditingActivity.this.fl_Sticker.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                ImageEditingActivity.this.mCurrentView.setInEdit(false);
                ImageEditingActivity.this.mCurrentView = stickerView;
                ImageEditingActivity.this.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = ImageEditingActivity.this.mViews.indexOf(stickerView);
                if (position != ImageEditingActivity.this.mViews.size() - 1) {
                    ImageEditingActivity.this.mViews.add(ImageEditingActivity.this.mViews.size(), (StickerView) ImageEditingActivity.this.mViews.remove(position));
                }
            }
        });
        this.fl_Sticker.addView(stickerView, new LayoutParams(-1, -1));
        this.mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (this.mCurrentView != null) {
            this.mCurrentView.setInEdit(false);
        }
        this.mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                if (((sourceBitmap.getPixel(x, y) >> 24) & 255) > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        if (maxX < minX || maxY < minY) {
            return null;
        }
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    private static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public void showStickerDialog() {
        final Dialog dial = new Dialog(this);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.sticker_dialog);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);
        ((ImageView) dial.findViewById(R.id.back_dialog)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dial.dismiss();
            }
        });
        ImageView c1 = (ImageView) dial.findViewById(R.id.c1);
        ImageView c2 = (ImageView) dial.findViewById(R.id.c2);
        ImageView c3 = (ImageView) dial.findViewById(R.id.c3);
        ImageView c4 = (ImageView) dial.findViewById(R.id.c4);
        ImageView c5 = (ImageView) dial.findViewById(R.id.c5);
        ImageView c6 = (ImageView) dial.findViewById(R.id.c6);
        this.stickerlist = new ArrayList();
        final GridView grid_sticker = (GridView) dial.findViewById(R.id.gridStickerList);
        setStickerList1();
        this.stickerAdapter = new StickerAdapter(getApplicationContext(), this.stickerlist);
        grid_sticker.setAdapter(this.stickerAdapter);
        c1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList1();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });
        c2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList2();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });
        c3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList3();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });
        c4.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList4();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });
        c5.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList5();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });
        c6.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageEditingActivity.this.stickerlist.clear();
                ImageEditingActivity.this.setStickerList6();
                ImageEditingActivity.this.stickerAdapter = new StickerAdapter(ImageEditingActivity.this.getApplicationContext(), ImageEditingActivity.this.stickerlist);
                grid_sticker.setAdapter(ImageEditingActivity.this.stickerAdapter);
            }
        });

        grid_sticker.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageEditingActivity.this.stickerId = ((Integer) ImageEditingActivity.this.stickerlist.get(i)).intValue();
                ImageEditingActivity.this.addStickerView(ImageEditingActivity.this.stickerId);
                dial.dismiss();
            }
        });
        dial.show();
    }

    private void setStickerList1() {
        this.stickerlist.add(Integer.valueOf(R.drawable.a4));
        this.stickerlist.add(Integer.valueOf(R.drawable.a2));
        this.stickerlist.add(Integer.valueOf(R.drawable.a3));
        this.stickerlist.add(Integer.valueOf(R.drawable.a6));
        this.stickerlist.add(Integer.valueOf(R.drawable.a9));
        this.stickerlist.add(Integer.valueOf(R.drawable.a7));
        this.stickerlist.add(Integer.valueOf(R.drawable.a1));
        this.stickerlist.add(Integer.valueOf(R.drawable.a10));
        this.stickerlist.add(Integer.valueOf(R.drawable.a16));
        this.stickerlist.add(Integer.valueOf(R.drawable.a8));
        this.stickerlist.add(Integer.valueOf(R.drawable.a14));
        this.stickerlist.add(Integer.valueOf(R.drawable.a15));
        this.stickerlist.add(Integer.valueOf(R.drawable.a12));
        this.stickerlist.add(Integer.valueOf(R.drawable.a21));
        this.stickerlist.add(Integer.valueOf(R.drawable.a18));
        this.stickerlist.add(Integer.valueOf(R.drawable.a19));
        this.stickerlist.add(Integer.valueOf(R.drawable.a20));
        this.stickerlist.add(Integer.valueOf(R.drawable.a26));
        this.stickerlist.add(Integer.valueOf(R.drawable.a22));
        this.stickerlist.add(Integer.valueOf(R.drawable.a24));
        this.stickerlist.add(Integer.valueOf(R.drawable.a17));
        this.stickerlist.add(Integer.valueOf(R.drawable.a28));
        this.stickerlist.add(Integer.valueOf(R.drawable.a25));
        this.stickerlist.add(Integer.valueOf(R.drawable.a29));
        this.stickerlist.add(Integer.valueOf(R.drawable.a30));
        this.stickerlist.add(Integer.valueOf(R.drawable.a32));
        this.stickerlist.add(Integer.valueOf(R.drawable.a34));
        this.stickerlist.add(Integer.valueOf(R.drawable.a35));
        this.stickerlist.add(Integer.valueOf(R.drawable.a36));
    }

    private void setStickerList2() {
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_02));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_07));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_01));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_04));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_05));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_03));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_11));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_08));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_06));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_10));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_14));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_12));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_13));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_18));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_15));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_09));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_17));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_16));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_19));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_25));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_20));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_22));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_23));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_24));
        this.stickerlist.add(Integer.valueOf(R.drawable.monster_21));
    }

    private void setStickerList3() {
        this.stickerlist.add(Integer.valueOf(R.drawable.l15));
        this.stickerlist.add(Integer.valueOf(R.drawable.l8));
        this.stickerlist.add(Integer.valueOf(R.drawable.l9));
        this.stickerlist.add(Integer.valueOf(R.drawable.l10));
        this.stickerlist.add(Integer.valueOf(R.drawable.l7));
        this.stickerlist.add(Integer.valueOf(R.drawable.l12));
        this.stickerlist.add(Integer.valueOf(R.drawable.l13));
        this.stickerlist.add(Integer.valueOf(R.drawable.l11));
        this.stickerlist.add(Integer.valueOf(R.drawable.l17));
        this.stickerlist.add(Integer.valueOf(R.drawable.l16));
        this.stickerlist.add(Integer.valueOf(R.drawable.l14));
        this.stickerlist.add(Integer.valueOf(R.drawable.l21));
        this.stickerlist.add(Integer.valueOf(R.drawable.l19));
        this.stickerlist.add(Integer.valueOf(R.drawable.l18));
        this.stickerlist.add(Integer.valueOf(R.drawable.l20));
        this.stickerlist.add(Integer.valueOf(R.drawable.l22));
        this.stickerlist.add(Integer.valueOf(R.drawable.l26));
        this.stickerlist.add(Integer.valueOf(R.drawable.l23));
        this.stickerlist.add(Integer.valueOf(R.drawable.l24));
        this.stickerlist.add(Integer.valueOf(R.drawable.l28));
        this.stickerlist.add(Integer.valueOf(R.drawable.l25));
        this.stickerlist.add(Integer.valueOf(R.drawable.l27));
        this.stickerlist.add(Integer.valueOf(R.drawable.l35));
        this.stickerlist.add(Integer.valueOf(R.drawable.l40));
        this.stickerlist.add(Integer.valueOf(R.drawable.l37));
        this.stickerlist.add(Integer.valueOf(R.drawable.l36));
        this.stickerlist.add(Integer.valueOf(R.drawable.l38));
        this.stickerlist.add(Integer.valueOf(R.drawable.l39));
        this.stickerlist.add(Integer.valueOf(R.drawable.l42));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_11));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_15));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_12));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_13));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_10));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_14));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_16));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_17));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_21));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_19));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_20));
        this.stickerlist.add(Integer.valueOf(R.drawable.cm_sticker_18));
    }

    private void setStickerList4() {
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding06));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding02));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding03));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding01));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding05));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding12));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding04));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding08));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding09));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding10));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding11));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding16));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding13));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding14));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding07));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding19));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding17));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding18));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding22));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding20));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding21));
        this.stickerlist.add(Integer.valueOf(R.drawable.wedding15));
    }

    private void setStickerList5() {
        this.stickerlist.add(Integer.valueOf(R.drawable.s3));
        this.stickerlist.add(Integer.valueOf(R.drawable.s2));
        this.stickerlist.add(Integer.valueOf(R.drawable.s8));
        this.stickerlist.add(Integer.valueOf(R.drawable.s1));
        this.stickerlist.add(Integer.valueOf(R.drawable.s5));
        this.stickerlist.add(Integer.valueOf(R.drawable.s6));
        this.stickerlist.add(Integer.valueOf(R.drawable.s4));
        this.stickerlist.add(Integer.valueOf(R.drawable.s7));
        this.stickerlist.add(Integer.valueOf(R.drawable.s9));
        this.stickerlist.add(Integer.valueOf(R.drawable.s13));
        this.stickerlist.add(Integer.valueOf(R.drawable.s10));
        this.stickerlist.add(Integer.valueOf(R.drawable.s12));
        this.stickerlist.add(Integer.valueOf(R.drawable.s16));
        this.stickerlist.add(Integer.valueOf(R.drawable.s14));
        this.stickerlist.add(Integer.valueOf(R.drawable.s11));
        this.stickerlist.add(Integer.valueOf(R.drawable.s20));
        this.stickerlist.add(Integer.valueOf(R.drawable.s17));
        this.stickerlist.add(Integer.valueOf(R.drawable.s15));
        this.stickerlist.add(Integer.valueOf(R.drawable.s18));
        this.stickerlist.add(Integer.valueOf(R.drawable.s19));
    }

    private void setStickerList6() {
        this.stickerlist.add(Integer.valueOf(R.drawable.b3));
        this.stickerlist.add(Integer.valueOf(R.drawable.b2));
        this.stickerlist.add(Integer.valueOf(R.drawable.b7));
        this.stickerlist.add(Integer.valueOf(R.drawable.b1));
        this.stickerlist.add(Integer.valueOf(R.drawable.b6));
        this.stickerlist.add(Integer.valueOf(R.drawable.b8));
        this.stickerlist.add(Integer.valueOf(R.drawable.b16));
        this.stickerlist.add(Integer.valueOf(R.drawable.b9));
        this.stickerlist.add(Integer.valueOf(R.drawable.b10));
        this.stickerlist.add(Integer.valueOf(R.drawable.b12));
        this.stickerlist.add(Integer.valueOf(R.drawable.b4));
        this.stickerlist.add(Integer.valueOf(R.drawable.b15));
        this.stickerlist.add(Integer.valueOf(R.drawable.b20));
        this.stickerlist.add(Integer.valueOf(R.drawable.b17));
        this.stickerlist.add(Integer.valueOf(R.drawable.b14));
        this.stickerlist.add(Integer.valueOf(R.drawable.b19));
        this.stickerlist.add(Integer.valueOf(R.drawable.b18));
    }
}
