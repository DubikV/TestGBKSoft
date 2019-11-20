package com.gmail.all.vanyadubik.testgbksoft.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.all.vanyadubik.testgbksoft.R;

import static com.gmail.all.vanyadubik.testgbksoft.common.Consts.TAG;

public class ActivityUtils {

    public static AlertDialog showMessage(Context mContext, String textTitle, Drawable drawableIconTitle,
                                          String textMessage) {
        if(mContext == null) return null;

        if (textMessage == null || textMessage.isEmpty()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.WhiteDialogTheme);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = layoutInflater.inflate(R.layout.dialog_title, null);
        ImageView imageTitle = titleView.findViewById(R.id.image_title);
        if(drawableIconTitle!=null){
            imageTitle.setImageDrawable(drawableIconTitle);
        }
        TextView titleTV = titleView.findViewById(R.id.text_title);
        titleTV.setText(TextUtils.isEmpty(textTitle) ?
                mContext.getString(R.string.questions_title_attention) :
                textTitle);
        builder.setCustomTitle(titleView);
        builder.setMessage(textMessage);

        builder.setNeutralButton(mContext.getString(R.string.questions_answer_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            dialog.show();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button1 = (Button) dialog.findViewById(android.R.id.button1);
        button1.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button2 = (Button) dialog.findViewById(android.R.id.button2);
        button2.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button3 = (Button) dialog.findViewById(android.R.id.button3);
        button3.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        return dialog;
    }

    public static void showShortToast(Context mContext, String message){
        if(mContext == null) return;
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context mContext, String message){
        if(mContext == null) return;
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void showQuestion(Context mContext, String textTitle, Drawable drawableIconTitle,
                                    String textMessage,
                                    String nameButton1, String nameButton2, String nameButton3,
                                    final QuestionAnswer questionAnswer) {
        if(mContext == null) return;
        if (textMessage == null || textMessage.isEmpty()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.WhiteDialogTheme);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = layoutInflater.inflate(R.layout.dialog_title, null);
        ImageView imageTitle = titleView.findViewById(R.id.image_title);
        if(drawableIconTitle==null){
            imageTitle.setVisibility(View.GONE);
        }else {
            imageTitle.setImageDrawable(drawableIconTitle);
        }
        TextView titleTV = titleView.findViewById(R.id.text_title);
        titleTV.setText(textTitle != null && !textTitle.isEmpty() ? textTitle :
                mContext.getString(R.string.questions_title_question));

        builder.setCustomTitle(titleView);
        builder.setMessage(textMessage);

        builder.setPositiveButton(TextUtils.isEmpty(nameButton1) ?
                mContext.getString(R.string.questions_answer_yes) : nameButton1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                questionAnswer.onPositiveAnswer();
            }
        });

        builder.setNegativeButton(TextUtils.isEmpty(nameButton2) ?
                mContext.getString(R.string.questions_answer_no) : nameButton2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                questionAnswer.onNegativeAnswer();
            }
        });

        if(!TextUtils.isEmpty(nameButton3)) {
            builder.setNeutralButton(nameButton3,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            questionAnswer.onNeutralAnswer();
                        }
                    });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                questionAnswer.onNegativeAnswer();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        try {
            dialog.show();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button1 = (Button) dialog.findViewById(android.R.id.button1);
        button1.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button2 = (Button) dialog.findViewById(android.R.id.button2);
        button2.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        Button button3 = (Button) dialog.findViewById(android.R.id.button3);
        button3.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
    }

    public static void hideKeyboard(Context context){
        if(context == null) return;
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static void showKeyboard(Context context, View view){
        if(context == null) return;
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static ProgressDialog showDialog(Context mContext, String title, String message){
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.WhiteDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if(!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if(!TextUtils.isEmpty(message)) {
            dialog.setMessage(message);
        }
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void showDialogText(Context mContext, String hint, OnEnterTextCallBack onEnterTextCallBack){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.WhiteDialogTheme);

        View titleView = LayoutInflater.from(mContext).inflate(R.layout.dialog_title, null);

        TextView titleTV = titleView.findViewById(R.id.text_title);
        titleTV.setText(hint);

        builder.setCustomTitle(titleView);

        final View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.layout_name, null);

        EditText nameET = viewInflated.findViewById(R.id.name);
        builder.setView(viewInflated);

        builder.setPositiveButton(mContext.getResources().getString(R.string.questions_answer_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.questions_answer_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        AlertDialog dialogPass = builder.create();
        dialogPass.show();
        dialogPass.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogPass.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        dialogPass.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v)-> {
            String text = nameET.getText().toString();
            if (TextUtils.isEmpty(text)) {
                nameET.setError(mContext.getString(R.string.error_field_required));
                return;
            }
            dialogPass.cancel();
            onEnterTextCallBack.onCallBack(text);
        });
        dialogPass.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        dialogPass.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v)-> {
            dialogPass.cancel();
        });
    }

    public interface OnEnterTextCallBack{
        void onCallBack(String text);
    }

    public interface QuestionAnswer {

        void onPositiveAnswer();

        void onNegativeAnswer();

        void onNeutralAnswer();

    }


}
