package com.br.virtualstore.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.br.virtualstore.R;


/**
 * Created by Alex on 20/11/2017.
 */

public class Utils {

    public static void showMsgToast(Activity activity, String txt) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View lytToast = inflater.inflate(R.layout.toast_template, (ViewGroup) activity.findViewById(R.id.lytToast));

        TextView txtToast = (TextView) lytToast.findViewById(R.id.txtToast);
        txtToast.setText(txt);

        Toast toast = new Toast(activity);
        toast.setView(lytToast);
        toast.show();
    }

    public static void showMsgSimpleToast(Activity activity, String txt) {
        Toast.makeText(activity, txt, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgConfirm(final Activity activity, String titulo, String txt, TipoMsg tipoMsg, DialogInterface.OnClickListener listener) {
        int theme = 0, icone = 0;
        switch (tipoMsg) {
            case SUCESSO:
                theme = R.style.AppTheme_Dark_Dialog_Sucesso;
                icone = R.drawable.success;
                break;
            case INFO:
                theme = R.style.AppTheme_Dark_Dialog_Info;
                icone = R.drawable.info;
                break;
            case ERRO:
                theme = R.style.AppTheme_Dark_Dialog_Error;
                icone = R.drawable.error;
                break;
            case ALERTA:
                theme = R.style.AppTheme_Dark_Dialog_Alert;
                icone = R.drawable.alert;
                break;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, theme).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(txt);
        alertDialog.setIcon(icone);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", listener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(params);
    }

    public static void showMsgAlertOK(final Activity activity, String titulo, String txt, TipoMsg tipoMsg) {
        int theme = 0, icone = 0;
        switch (tipoMsg) {
            case SUCESSO:
                theme = R.style.AppTheme_Dark_Dialog_Sucesso;
                icone = R.drawable.success;
                break;
            case INFO:
                theme = R.style.AppTheme_Dark_Dialog_Info;
                icone = R.drawable.info;
                break;
            case ERRO:
                theme = R.style.AppTheme_Dark_Dialog_Error;
                icone = R.drawable.error;
                break;
            case ALERTA:
                theme = R.style.AppTheme_Dark_Dialog_Alert;
                icone = R.drawable.alert;
                break;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, theme).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(txt);
        alertDialog.setIcon(icone);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(params);
    }

}
