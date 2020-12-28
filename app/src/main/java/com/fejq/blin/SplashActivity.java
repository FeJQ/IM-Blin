package com.fejq.blin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.fejq.utils.UtilAes;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void checkUserToken() throws Exception
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        String cipherUserName = sharedPreferences.getString(getString(R.string.shared_user_name), null);
        String cipherPassword = sharedPreferences.getString(getString(R.string.shared_password), null);
        byte[] key = getAesKey();
        String plainUserName = UtilAes.decrypt(cipherUserName, key);
        String plainPassword = UtilAes.encrypt(cipherPassword, key);

    }

    private byte[] getAesKey()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Blin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = sharedPreferences.getString(getString(R.string.shared_aes_key_name), null);

        if (key != null)
        {
            return key.getBytes();
        }
        byte[] newKey = UtilAes.generateKey();

        editor.putString(getString(R.string.shared_aes_key_name), newKey.toString());
        return newKey;
    }
}