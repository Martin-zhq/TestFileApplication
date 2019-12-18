package cn.xxt.testfileapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** 展示结果 */
    private TextView tvShowResult;

    private static final String FILE_NAME = "hello.txt";
    private static final String CONTENT = "来验证AndroidQ文件存储到底有啥牛逼的~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShowResult = findViewById(R.id.tv_show_result);
    }

    public void requestPermission(View view) {
        XXPermissions.with(this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.Group.STORAGE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "获取权限成功，部分权限未正常授予", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            Toast.makeText(MainActivity.this, "被永久拒绝授权，请手动授予权限", Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(MainActivity.this);
                        }else {
                            Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void isHasPermission(View view) {
        if (XXPermissions.isHasPermission(MainActivity.this, Permission.Group.STORAGE)) {
            Toast.makeText(MainActivity.this, "已经获取到权限，不需要再次申请了", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "还没有获取到权限或者部分权限未授予", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoPermissionSettings(View view) {
        XXPermissions.gotoPermissionSettings(MainActivity.this);
    }

    /*****内存****内存****内存****内存**内存*内存****内存*内存*******内存*********内存****内存*/
    /** 内存的根目录是：/data/data/PackageName **/
    /**
     * 利用openFileOutput方法往  内存   写文件
     * 结果会生成  /data/data/PackageName/files/文件名
     * @param view
     */
    public void inOpenFileOutput(View view) {

        try {
            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(CONTENT.getBytes());
            fileOutputStream.close();

            //结果提示成功
            tvShowResult.setText("内存, openFileOutPut 生成文件成功~");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "文件找不到", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 内存：openFileInput方法读取文件
     * @param view
     */
    public void inOpenFileInput(View view) {
        try {
            FileInputStream fileInputStream = openFileInput(FILE_NAME);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuffer sb = new StringBuffer("");

            while ((hasRead = fileInputStream.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, hasRead));
            }
            fileInputStream.close();

            tvShowResult.setText("内存,openFileInput 读取文件成功：" + sb);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存读文件出现异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 内存：getFilesDir()方法，写入文件
     * @param view
     */
    public void inGetFilesDir(View view) {
        try {
            File file = new File(getFilesDir(), "infos.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String str = "测试内存方法getFilesDir往AndroidQ中写入文件";
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

            tvShowResult.setText("内存，getFilesDir 写入文件成功~");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存getFilesDir方法写入文件出错", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 内存：fileList方法查看内存中文件列表
     * @param view
     */
    public void inFileList(View view) {
        try {
            String[] fileList = fileList();
            String s = "";
            if (fileList.length > 0) {
                for (String s1 : fileList) {
                    s = s + s1 + "\n";
                }

                tvShowResult.setText(s);
            } else {
                tvShowResult.setText("内存中没有文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存fileList出现异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 内存删除文件
     * @param view
     */
    public void inDeleteFile(View view) {
        try {
            boolean result = deleteFile(FILE_NAME);
            if (result) {
                tvShowResult.setText("删除hello.txt成功");
            } else {
                tvShowResult.setText("删除hello.txt失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存deleteFile删除文件异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 往内存cache文件夹下创建文件
     * @param view
     */
    public void inGetCacheDir(View view) {
        try {
            File file = new File(getCacheDir(), "infos.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String str = "往内存cache文件夹下创建infos.txt文件";
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

            tvShowResult.setText("往内存cache文件夹下创建infos.txt文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存getCacheDir出现异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 内存，自定义文件夹，会带app_前缀
     * @param view
     */
    public void inGetDir(View view) {
        try {
            File file = new File(getDir("test", MODE_PRIVATE), "app.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String str = "自定义的文件夹，会被自动带上app_前缀。。";
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

            tvShowResult.setText("内存 getDir 写入文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内存自定义app_前缀文件夹异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**外存****外存****外存****外存****外存****外存****外存****外存****外存****外存****外存****外存**/


    /**
     * 往外存公共文件夹下的download文件夹中写文件
     * @param view
     */
    public void outGetExternalStoragePublicDictionary(View view) {
        if (isExternalStorageWritable()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS
                    ), "zhqwenjianja");

            if (file.mkdirs()) {
                //文件夹创建成功
                File file1 = new File(file, "zhq.txt");
                String str = "往外存download文件夹下，新创建文件夹和文件，并写入数据";
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.close();

                    tvShowResult.setText("外存getExternalStoragePublicDictionary写入download文件夹文件成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    tvShowResult.setText("外存getExternalStoragePublicDictionary出现异常");
                }
            } else {
                tvShowResult.setText("外存Download文件夹下zhqwenjianjia未创建成功");
            }

        } else {
            tvShowResult.setText("外存设备介质不可用");
        }
    }


    /**
     * 外存，应用私有目录写入文件
     * @param view
     */
    public void outGetExternalFilesDir(View view) {
        try {
            File file = new File(getExternalFilesDir(null), "zhq.txt");
            String str = "外存，应用私有目录写入数据";
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(str.getBytes());
            outputStream.close();

            tvShowResult.setText("外存，应用私有目录getExternalFilesDir写入数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "外存getExternalFilesDir出现异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 外存，getExternalCacheDir
     */
    public void outGetExternalCacheDir(View view) {
        try {
            File file = new File(getExternalCacheDir(), "zhq.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String str = "往外存的cache文件夹中创建文件，并写入数据";
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

            tvShowResult.setText("外存，getExternalCacheDir写入文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "外存getExternalCacheDir出现异常", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 检查设备介质是否可用
     * @return
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }
}
