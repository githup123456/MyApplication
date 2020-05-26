package com.example.administrator.myapplication.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FragmentView extends View{
        private Context context;
        public FragmentView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            this.context=context;
        }
        private int containerId;

        private List<Class> fragmentClassList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();
        private List<Integer> iconResBeforeList = new ArrayList<>();
        private List<Integer> iconResAfterList = new ArrayList<>();

        private List<Fragment> fragmentList = new ArrayList<>();

        private int itemCount;

        private Paint paint = new Paint();

        private List<Bitmap> iconBitmapBeforeList = new ArrayList<>();
        private List<Bitmap> iconBitmapAfterList = new ArrayList<>();
        private List<Rect> iconRectList = new ArrayList<>();

        private int currentCheckedIndex;
        private int firstCheckedIndex;

        private int titleColorBefore = Color.parseColor("#ffffff");
        private int titleColorAfter = Color.parseColor("#ffffff");

        private int titleSizeInDp = 13;
        private int iconWidth = 20;
        private int iconHeight = 20;
        private int titleIconMargin = 5;

        public FragmentView setContainer(int containerId) {
            this.containerId = containerId;
            return this;
        }

        public FragmentView setTitleBeforeAndAfterColor(String beforeResCode, String AfterResCode) {//鏀寔"#333333"杩欑褰㈠紡
            titleColorBefore = Color.parseColor(beforeResCode);
            titleColorAfter = Color.parseColor(AfterResCode);
            return this;
        }

        public FragmentView setTitleSize(int titleSizeInDp) {
            this.titleSizeInDp = titleSizeInDp;
            return this;
        }

        public FragmentView setIconWidth(int iconWidth) {
            this.iconWidth = iconWidth;
            return this;
        }

        public FragmentView setTitleIconMargin(int titleIconMargin) {
            this.titleIconMargin = titleIconMargin;
            return this;
        }

        public FragmentView setIconHeight(int iconHeight) {
            this.iconHeight = iconHeight;
            return this;
        }

        public FragmentView addItem(Class fragmentClass, String title, int iconResBefore, int iconResAfter) {
            fragmentClassList.add(fragmentClass);
            titleList.add(title);
            iconResBeforeList.add(iconResBefore);
            iconResAfterList.add(iconResAfter);
            return this;
        }

        public FragmentView setFirstChecked(int firstCheckedIndex) {//浠?寮€濮?
            this.firstCheckedIndex = firstCheckedIndex;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void build() {
            itemCount = fragmentClassList.size();
            //棰勫垱寤篵itmap鐨凴ect骞剁紦瀛?
            //棰勫垱寤篿con鐨凴ect骞剁紦瀛?
            for (int i = 0; i < itemCount; i++) {
                Bitmap beforeBitmap = getBitmap(iconResBeforeList.get(i));
                iconBitmapBeforeList.add(beforeBitmap);

                Bitmap afterBitmap = getBitmap(iconResAfterList.get(i));
                iconBitmapAfterList.add(afterBitmap);

                Rect rect = new Rect();
                iconRectList.add(rect);

                Class clx = fragmentClassList.get(i);
                try {
                    Fragment fragment = (Fragment) clx.newInstance();
                    fragmentList.add(fragment);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            currentCheckedIndex = firstCheckedIndex;
            switchFragment(currentCheckedIndex);

            invalidate();
        }

        private Bitmap getBitmap(int resId) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(resId);
            return bitmapDrawable.getBitmap();
        }

        //////////////////////////////////////////////////
        //鍒濆鍖栨暟鎹熀纭€
        //////////////////////////////////////////////////

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            initParam();
        }

        private int titleBaseLine;
        private List<Integer> titleXList = new ArrayList<>();

        private int parentItemWidth;

        private void initParam() {
            if (itemCount != 0) {
                //鍗曚釜item瀹介珮
                parentItemWidth = getWidth() / itemCount;
                int parentItemHeight = getHeight();

                //鍥炬爣杈归暱
                int iconWidth = dp2px(this.iconWidth);//鍏堟寚瀹?0dp
                int iconHeight = dp2px(this.iconHeight);

                //鍥炬爣鏂囧瓧margin
                int textIconMargin = dp2px(((float)titleIconMargin)/2);//鍏堟寚瀹?dp锛岃繖閲岄櫎浠ヤ竴鍗婃墠鏄甯哥殑margin锛屼笉鐭ラ亾涓哄暐锛屽彲鑳芥槸鍥剧墖鐨勫師鍥?

                //鏍囬楂樺害
                int titleSize = dp2px(titleSizeInDp);//杩欓噷鍏堟寚瀹?0dp
                paint.setTextSize(titleSize);
                Rect rect = new Rect();
                paint.getTextBounds(titleList.get(0), 0, titleList.get(0).length(), rect);
                int titleHeight = rect.height();

                //浠庤€岃绠楀緱鍑哄浘鏍囩殑璧峰top鍧愭爣銆佹枃鏈殑baseLine
                int iconTop = (parentItemHeight - iconHeight - textIconMargin - titleHeight)/2;
                titleBaseLine = parentItemHeight - iconTop;

                //瀵筰con鐨剅ect鐨勫弬鏁拌繘琛岃祴鍊?
                int firstRectX = (parentItemWidth - iconWidth) / 2;//绗竴涓猧con鐨勫乏
                for (int i = 0; i < itemCount; i++) {
                    int rectX = i * parentItemWidth + firstRectX;

                    Rect temp = iconRectList.get(i);

                    temp.left = rectX;
                    temp.top = iconTop ;
                    temp.right = rectX + iconWidth;
                    temp.bottom = iconTop + iconHeight;
                }

                //鏍囬锛堝崟浣嶆槸涓棶棰橈級
                for (int i = 0; i < itemCount; i ++) {
                    String title = titleList.get(i);
                    paint.getTextBounds(title, 0, title.length(), rect);
                    titleXList.add((parentItemWidth - rect.width()) / 2 + parentItemWidth * i);
                }
            }
        }

        private int dp2px(float dpValue) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        //////////////////////////////////////////////////
        //鏍规嵁寰楀埌鐨勫弬鏁扮粯鍒?
        //////////////////////////////////////////////////

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);//杩欓噷璁﹙iew鑷韩鏇挎垜浠敾鑳屾櫙 濡傛灉鎸囧畾鐨勮瘽

            if (itemCount != 0) {
                //鐢昏儗鏅?
                paint.setAntiAlias(false);
                for (int i = 0; i < itemCount; i++) {
                    Bitmap bitmap = null;
                    if (i == currentCheckedIndex) {
                        bitmap = iconBitmapAfterList.get(i);
                    } else {
                        bitmap = iconBitmapBeforeList.get(i);
                    }
                    Rect rect = iconRectList.get(i);
                    canvas.drawBitmap(bitmap, null, rect, paint);//null浠ｈ〃bitmap鍏ㄩ儴鐢诲嚭
                }

                //鐢绘枃瀛?
                paint.setAntiAlias(true);
                for (int i = 0; i < itemCount; i ++) {
                    String title = titleList.get(i);
                    if (i == currentCheckedIndex) {
                        paint.setColor(titleColorAfter);
                    } else {
                        paint.setColor(titleColorBefore);
                    }
                    int x = titleXList.get(i);
                    canvas.drawText(title, x, titleBaseLine, paint);
                }
            }
        }

        //////////////////////////////////////////////////
        //鐐瑰嚮浜嬩欢:鎴戣瀵熶簡寰崥鍜屾帉鐩燂紝鍙戠幇down鍜寀p閮藉湪璇ュ尯鍩熷唴鎵嶅搷搴?
        //////////////////////////////////////////////////

        int target = -1;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    target = withinWhichArea((int)event.getX());
                    break;
                case MotionEvent.ACTION_UP :
                    if (event.getY() < 0) {
                        break;
                    }
                    if (target == withinWhichArea((int)event.getX())) {
                        //杩欓噷瑙﹀彂鐐瑰嚮浜嬩欢
                        switchFragment(target);
                        currentCheckedIndex = target;
                        invalidate();
                    }
                    target = -1;
                    break;
            }
            return true;
            //杩欓噷return super涓轰粈涔坲p鎵ц涓嶅埌锛熸槸鍥犱负return super鐨勫€硷紝鍏ㄩ儴鍙栧喅浜庝綘鏄惁
            //clickable锛屽綋浣燿own浜嬩欢鏉ヤ复锛屼笉鍙偣鍑伙紝鎵€浠eturn false锛屼篃灏辨槸璇达紝鑰屼笖浣犳病
            //鏈夎缃畂nTouchListener锛屽苟涓旀帶浠舵槸ENABLE鐨勶紝鎵€浠ispatchTouchEvent鐨勮繑鍥炲€?
            //涔熸槸false锛屾墍浠ュ湪view group鐨刣ispatchTransformedTouchEvent涔熸槸杩斿洖false锛?
            //杩欐牱涓€鏉ワ紝view group涓殑first touch target灏辨槸绌虹殑锛屾墍浠ntercept鏍囪浣?
            //鏋滄柇涓篺alse锛岀劧鍚庡氨鍐嶄篃杩涗笉鍒板惊鐜彇瀛╁瓙鐨勬楠や簡锛岀洿鎺ヨ皟鐢╠ispatch-
            // TransformedTouchEvent骞朵紶瀛╁瓙涓簄ull锛屾墍浠ョ洿鎺ヨ皟鐢╲iew group鑷韩鐨刣ispatch-
            // TouchEvent浜?
        }

        private int withinWhichArea(int x) { return x/parentItemWidth; }//浠?寮€濮?

        //////////////////////////////////////////////////
        //纰庣墖澶勭悊浠ｇ爜
        //////////////////////////////////////////////////
        private Fragment currentFragment;

        //娉ㄦ剰 杩欓噷鏄彧鏀寔AppCompatActivity 闇€瑕佹敮鎸佸叾浠栬€佺増鐨?鑷淇敼
        protected void switchFragment(int whichFragment) {
            Fragment fragment = fragmentList.get(whichFragment);
            int frameLayoutId = containerId;

            if (fragment != null) {
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                if (fragment.isAdded()) {
                    if (currentFragment != null) {
                        transaction.hide(currentFragment).show(fragment);
                    } else {
                        transaction.show(fragment);
                    }
                } else {
                    if (currentFragment != null) {
                        transaction.hide(currentFragment).add(frameLayoutId, fragment);
                    } else {
                        transaction.add(frameLayoutId, fragment);
                    }
                }
                currentFragment = fragment;
                transaction.commit();
            }
        }


}
