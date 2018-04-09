package com.example.hiroyki.newsviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

class ImageGetTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView image;
    private String tag;

    public ImageGetTask(ImageView _image) {
        image = _image;
        tag = image.getTag().toString();
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image;
        try {
            //キャッシュより画像データを取得
            image = ImageCache.getImage(params[0]);
            if (image == null) {
                URL imageUrl = new URL(params[0]);
                InputStream imageIs;
                imageIs = imageUrl.openStream();

                // 画像サイズ情報を取得する
                BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                imageOptions.inJustDecodeBounds = true;
                imageOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                //BitmapFactory.decodeStream(imageIs, null, imageOptions);
                //Log.v("image", "Original Image Size: " + imageOptions.outWidth + " x " + imageOptions.outHeight);


                // もし、画像が大きかったら縮小して読み込む
                //  今回はimageSizeMaxの大きさに合わせる
                int imageSizeMax = 500;
                float imageScaleWidth = (float) imageOptions.outWidth / imageSizeMax;
                float imageScaleHeight = (float) imageOptions.outHeight / imageSizeMax;

                // もしも、縮小できるサイズならば、縮小して読み込む
                if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                    BitmapFactory.Options imageOptions2 = new BitmapFactory.Options();

                    // 縦横、小さい方に縮小するスケールを合わせる
                    int imageScale = (int) Math.floor((imageScaleWidth > imageScaleHeight ? imageScaleHeight : imageScaleWidth));

                    // inSampleSizeには2のべき上が入るべきなので、imageScaleに最も近く、かつそれ以下の2のべき上の数を探す
                    for (int i = 2; i <= imageScale; i *= 2) {
                        imageOptions2.inSampleSize = i;
                    }

                    image = BitmapFactory.decodeStream(imageIs, null, imageOptions2);
                    //取得した画像データをキャッシュに保持
                    ImageCache.setImage(params[0], image);
                } else {
                    image = BitmapFactory.decodeStream(imageIs);
                    //取得した画像データをキャッシュに保持
                    ImageCache.setImage(params[0], image);
                }
            }
            return image;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        // Tagが同じものか確認して、同じであれば画像を設定する
        // （Tagの設定をしないと別の行に画像が表示されてしまう）
        if (tag.equals(image.getTag())) {
            if (result != null) {
                //画像の設定
                // 取得した画像をImageViewに設定します。
                image.setImageBitmap(result);
            }
            image.setVisibility(View.VISIBLE);
        }
    }
}

class ImageCache {
    private static HashMap<String,Bitmap> cache = new HashMap<String,Bitmap>();

    //キャッシュより画像データを取得
    public static Bitmap getImage(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        //存在しない場合はNULLを返す
        return null;
    }

    //キャッシュに画像データを設定
    public static void setImage(String key, Bitmap image) {
        cache.put(key, image);
    }

    //キャッシュの初期化（リスト選択終了時に呼び出し、キャッシュで使用していたメモリを解放する）
    public static void clearCache(){
        cache = null;
        cache = new HashMap<String,Bitmap>();
    }
}
