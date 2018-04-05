package argenisferrer.example.com.sampleapp;

/**
 *  Do not remove code above this
 */

import java.util.Random;

public class Sticker {

    private int[] mArrayOfStickers = new int[6];
    private int mSelectedIndex = -1;
    private boolean mAlwaysGenerateNew = false;

    public Sticker(){
        setArrayOfStickers();
    }

    public Sticker(Boolean generateNew){
        mAlwaysGenerateNew = generateNew;
        setArrayOfStickers();
    }

    public int[] getArrayOfStickers() {
        return mArrayOfStickers;
    }

    public void setArrayOfStickers() {
        mArrayOfStickers[0] = R.mipmap.no_luck;
        mArrayOfStickers[1] = R.mipmap.java;
        mArrayOfStickers[2] = R.mipmap.burger;
        mArrayOfStickers[3] = R.mipmap.muffin;
        mArrayOfStickers[4] = R.mipmap.group;
        mArrayOfStickers[5] = R.mipmap.java;
    }

    public int getSticketId(){
        if(mAlwaysGenerateNew || mSelectedIndex == -1){
            Random rand = new Random();
            mSelectedIndex = rand.nextInt(mArrayOfStickers.length);
        }
        return mArrayOfStickers[mSelectedIndex];
    }

    public String getStickerName(int id){
        String name = "Java";

        if(id == R.mipmap.no_luck){
            name = "No luck";
        }else if (id == R.mipmap.burger){
            name = "Burger";
        }else if (id == R.mipmap.muffin){
            name = "Muffin";
        }else if (id == R.mipmap.group){
            name = "EMEA group";
        }

        return name;
    }

}