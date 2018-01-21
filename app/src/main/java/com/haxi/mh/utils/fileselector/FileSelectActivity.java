package com.haxi.mh.utils.fileselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haxi.mh.R;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.utils.model.FileUtil;
import com.haxi.mh.utils.model.SdCardUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/*

用法：

Intent intent = new Intent(mActivity, FileSelectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FILE);
intent.putExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, true);
startActivityForResult(intent, FILE_SELECT_CODE);

在onActivityResult中回调
if (resultCode == Activity.RESULT_OK) {
    final ArrayList<String> uris = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
}

* */

/**
 * 文件选择器的activity
 * Created by Han on 2018/1/5
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class FileSelectActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {

    public static final String TAG = "FileSelectActivity";

    /**
     * 选择文件模式下的标题，默认是选择文件
     **/
    private String mSelectorFileTitle = FileSelectConstant.SELECTOR_MODE_FILE_TITLE;

    /**
     * 选择文件夹路径模式下的标题，默认是选择文件夹
     **/
    private String mSelectorFolderTitle = FileSelectConstant.SELECTOR_MODE_FOLDER_TITLE;

    /**
     * 根目录名称，默认是"设备存储："
     **/
    private String mSelectorRootPathName = FileSelectConstant.SELECTOR_ROOT_PATH;

    /**
     * 选择file还是选择folder，默认选择文件
     **/
    private int mSelectorMode = FileSelectConstant.SELECTOR_MODE_FILE;

    /**
     * 选择器文件的图标
     **/
    private int mSelectorFileIcon = R.drawable.fileselect_file;

    /**
     * 选择器文件夹的图标
     **/
    private int mSelectorFolderIcon = R.drawable.fileselect_folder;

    /**
     * 文件或者文件夹图标的宽度，默认-1，是根据图标大小填充宽度
     **/
    private int mSelectorIconWidth = -1;

    /**
     * 文件或者文件夹图标的高度，默认-1，是根据图标大小填充高度
     **/
    private int mSelectorIconHeight = -1;

    /**
     * 是否多选
     **/
    private boolean mSelectorIsMultiple = false;

    /** actionbar **/
    //	private ActionBar actionbar;

    /**
     * 当前folder路径
     **/
    private TextView mFolderPath_tv;

    /**
     * 文件选择listview
     **/
    private ListView mFileSelectListView;

    /**
     * 自定义文件选择listview的adapter
     **/
    private FileSelectAdapter mAdapter;

    private boolean isFileOnClickShowOk = true;

    private String[] mFrom;
    private int[] mTo;

    private List<Map<String, Object>> mData;

    /**
     * 所有存储路径
     **/
    private List<String> rootPaths;

    TextView tv_mode_file;
    TextView tv_fileselector_ok;
    ImageView title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        initIntent();
        initParams();
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化actionbar
     */
    //	protected void initActionBar() {
    //		actionbar = getSupportActionBar();
    //		actionbar.setHomeButtonEnabled(true);// 设置返回键可用
    //		actionbar.setDisplayHomeAsUpEnabled(true);
    //		actionbar.setDisplayShowHomeEnabled(true);
    //
    //	}

    /**
     * 初始化Intent相关bundle
     */
    private void initIntent() {
        // 获取文件请求码，默认是选择文件100
        mSelectorMode = getIntent().getIntExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FILE);
        mSelectorIsMultiple = getIntent().getBooleanExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, false);
    }

    /**
     * 子类要复写这个方法
     * <p>
     * 初始化一些参数，比如标题、按钮文字、文件夹或者文件的图标（默认只有只区分文件和文件夹，不区分文件后缀）
     * <p>
     * setSelectorFileTitle("this is file title");
     * setSelectorFolderTitle("this is folder title");
     * setSelectorFileIcon(R.drawable.ic_fileselect_file);
     * setSelectorFolderIcon(R.drawable.ic_fileselect_folder);
     * setSelectorIconWidth(100);
     * setSelectorIconHeight(100);
     */
    public void initParams() {

    }

    /**
     * 初始化当前路径的textview和列表的listview
     */
    private void initView() {
        mFolderPath_tv = (TextView) findViewById(R.id.id_fileselect_folderpath);
        mFileSelectListView = (ListView) findViewById(R.id.id_fileselect_listview);
        title_back = (ImageView) findViewById(R.id.img_title_back);
        tv_mode_file = (TextView) findViewById(R.id.mode_file);
        tv_fileselector_ok = (TextView) findViewById(R.id.fileselector_ok);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FILE) {

            tv_mode_file.setText(mSelectorFileTitle);// 设置标题为"选择文档"

        } else if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FOLDER) {

            tv_mode_file.setText(mSelectorFolderTitle);// 设置标题为"选择路径"

        }

        mFolderPath_tv.setText(mSelectorRootPathName);

        mFrom = new String[]{"icon", " filename", "childnum", "createtime", "fun"};
        mTo = new int[]{R.id.id_fileselect_icon, R.id.id_fileselect_filename,
                R.id.id_fileselect_childnum, R.id.id_fileselect_createtime,
                R.id.id_fileselect_fun};

        rootPaths = SdCardUtil.sdList;

        mData = getDataByFolderPath(mSelectorRootPathName);

        mAdapter = new FileSelectAdapter(this, mData, R.layout.adapter_fileselect_item, mFrom, mTo);
        mAdapter.setSelectorMode(mSelectorMode);
        mAdapter.setSelectorIsMultiple(mSelectorIsMultiple);

        if (mSelectorIconWidth != -1) {
            mAdapter.setSelectorIconWidth(mSelectorIconWidth);
        }
        if (mSelectorIconHeight != -1) {
            mAdapter.setSelectorIconHeight(mSelectorIconHeight);
        }

        mFileSelectListView.setAdapter(mAdapter);

    }

    /**
     * 初始化listview的点击事件
     */
    private void initEvent() {
        mFileSelectListView.setOnItemClickListener(this);
        tv_fileselector_ok.setOnClickListener(this);
        tv_mode_file.setOnClickListener(this);
        title_back.setOnClickListener(this);
    }

    /**
     * 根据上级文件夹路径获取所有的子节点数据
     *
     * @param parentFolderPath 上级文件夹路径
     * @return List<Map<String,Object>>数据
     */
    public List<Map<String, Object>> getDataByFolderPath(String parentFolderPath) {
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();

        // 根目录
        if (parentFolderPath.equals(mSelectorRootPathName)) {

            for (Storage storage : SdCardUtil.getStorages()) {
                File storageFile = new File(storage.getAbsPath());

                String childNum = "";

                List<File> fileList = new ArrayList<File>();

                File[] files = storageFile.listFiles();
                if (null == files) {
                    throw new NullPointerException(
                            "Error: File[] files is null, please make sure that you have been added the two permissions: WRITE_EXTERNAL_STORAGE and READ_EXTERNAL_STORAGE!!!");
                }
                for (File child : files) {
                    if (child.getName().startsWith(".") || !child.exists()) {
                        continue;
                    }
                    if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FILE) {
                        fileList.add(child);
                    } else if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FOLDER) {
                        if (child.isDirectory()) {
                            fileList.add(child);
                        }
                    }
                }
                // 排序
                FileUtil.sortByName(fileList);

                childNum = "共 " + fileList.size() + " 项";
                Map<String, Object> map = new HashMap<String, Object>();

                if (storageFile.isFile()) {
                    String fileType = storage.getLocalName().substring(storage.getLocalName().lastIndexOf(".") + 1);
                    if (Constant.WORD.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_word_36);
                    } else if (Constant.EXCEL.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_excel_36);
                    } else if (Constant.PPT.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_ppt_36);
                    } else if (Constant.TXT.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_txt_36);
                    } else if (Constant.PDF.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_pdf_36);
                    } else if (Constant.ZIP.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_zip_36);
                    } else if (Constant.PIC.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_pic_36);
                    } else if (Constant.VIDEO.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_video_36);
                    } else if (Constant.MUSIC.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_music_36);
                    } else {
                        map.put(mFrom[0], R.drawable.file_unknown_36);
                    }
                } else {
                    map.put(mFrom[0], mSelectorFolderIcon);
                }
                map.put(mFrom[1], storage.getLocalName());
                map.put(mFrom[2], childNum);
                map.put(mFrom[3], getLocalDateByMilliseconds(storageFile.lastModified(), "yyyy-MM-dd"));
                map.put(mFrom[4], false);
                map.put("file", storageFile);

                datalist.add(map);
            }

        } else {

            List<File> fileList = new ArrayList<File>();// 只有文件夹

            parentFolderPath = SdCardUtil.replaceLocalNameWithAbsPath(parentFolderPath);
            File folderPath = new File(parentFolderPath);
            File[] files = folderPath.listFiles();
            for (File child : files) {
                if (child.getName().startsWith(".") || !child.exists()) {
                    continue;
                }
                if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FILE) {
                    fileList.add(child);
                } else if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FOLDER) {
                    if (child.isDirectory()) {
                        fileList.add(child);
                    }
                }
            }
            // 排序
            FileUtil.sortByName(fileList);

            for (int i = 0; i < fileList.size(); i++) {
                File file = fileList.get(i);
                String childNum = null;
                if (file.isDirectory()) {
                    List<File> childFileList = new ArrayList<File>();
                    File[] childFiles = file.listFiles();
                    for (File child : childFiles) {
                        if (child.getName().startsWith(".") || !child.exists()) {
                            continue;
                        }
                        if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FILE) {
                            childFileList.add(child);
                        } else if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FOLDER) {
                            if (child.isDirectory()) {
                                childFileList.add(child);
                            }
                        }
                    }
                    childNum = "共 " + childFileList.size() + " 项";
                } else if (file.isFile()) {
                    childNum = FileUtil.convertFileSize(file.length());
                }

                // 根据其他file添加记录
                Map<String, Object> map = new HashMap<String, Object>();
                if (file.isFile()) {
                    String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    if (Constant.WORD.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_word_36);
                    } else if (Constant.EXCEL.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_excel_36);
                    } else if (Constant.PPT.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_ppt_36);
                    } else if (Constant.TXT.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_txt_36);
                    } else if (Constant.PDF.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_pdf_36);
                    } else if (Constant.ZIP.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_zip_36);
                    } else if (Constant.PIC.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_pic_36);
                    } else if (Constant.VIDEO.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_video_36);
                    } else if (Constant.MUSIC.contains(fileType)) {
                        map.put(mFrom[0], R.drawable.file_music_36);
                    } else {
                        map.put(mFrom[0], R.drawable.file_unknown_36);
                    }
                } else {
                    map.put(mFrom[0], mSelectorFolderIcon);
                }

                map.put(mFrom[1], file.getName());
                map.put(mFrom[2], childNum);
                map.put(mFrom[3], getLocalDateByMilliseconds(file.lastModified(), "yyyy-MM-dd"));
                map.put(mFrom[4], false);
                map.put("file", file);

                datalist.add(map);
            }
        }
        return datalist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_select, menu);
        MenuItem item_ok = menu.findItem(R.id.action_fileselect_ok);
        item_ok.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    /**
     * actionbar 返回键和完成键的操作
     */
    //	@Override
    //	public boolean onOptionsItemSelected(MenuItem item) {
    //		int id = item.getItemId();
    //		if (id == android.R.id.home) {
    //			this.finish();
    //			return true;
    //		} else if (id == R.id.action_fileselect_ok) {
    //			onClickOkBtn();
    //			return true;
    //		} else {
    //			return super.onOptionsItemSelected(item);
    //		}
    //	}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mData = getDataByFolderPath(mFolderPath_tv.getText().toString());
        File file = (File) mData.get(position).get("file");
        if (file.isFile()) {
            if (!isFileOnClickShowOk) {
                // 当点击file的时候直接返回结果
            } else {
                // 当点击file的时候出现“完成”按钮
                // 选中状态
                // onItemSelected(parent, view, position, id);
            }
        } else if (file.isDirectory()) {
            String parentPath = file.getAbsolutePath();
            parentPath = SdCardUtil.replaceAbsPathWithLocalName(parentPath);
            mFolderPath_tv.setText(parentPath);
            refreshByParentPath(parentPath);
        }
    }

    /**
     * 刷新文件夹
     *
     * @param parentPath 点击要进入的文件夹
     */
    public void refreshByParentPath(String parentPath) {

        mAdapter.setData(getDataByFolderPath(parentPath));
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 点击"完成"按钮后的操作
     */
    public void onClickOkBtn() {
        if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FILE) {
            ArrayList<String> fileList = new ArrayList<String>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                Map<String, Object> map = mAdapter.getData().get(i);
                boolean isChecked = (boolean) map.get(mFrom[4]);
                File file = (File) map.get("file");
                if (file.isFile() && isChecked) {
                    fileList.add(file.getAbsolutePath());
                }
            }
            if (fileList.isEmpty()) {
                Toast.makeText(this, "您尚未选取文件，请选择后重试", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS, fileList);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            }

        } else if (mSelectorMode == FileSelectConstant.SELECTOR_MODE_FOLDER) {
            ArrayList<String> fileList = new ArrayList<String>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                Map<String, Object> map = mAdapter.getData().get(i);
                boolean isChecked = (boolean) map.get(mFrom[4]);
                File file = (File) map.get("file");
                if (file.isDirectory() && isChecked) {
                    fileList.add(file.getAbsolutePath());
                }
            }

            if (fileList.isEmpty()) {
                Log.e(TAG, "fileList.size(): " + fileList.size() + "，选择路径错误");
                Toast.makeText(this, "您尚未选取文档路径，请选择后重试", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS, fileList);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            }
        }
    }

    /**
     * 返回键的操作（到根节点的时候finish，其他返回到上一级）
     */
    @Override
    public void onBackPressed() {
        backOrExit();
    }

    /**
     * 返回上一级或者退出当前activity
     */
    public void backOrExit() {
        String folderPath = mFolderPath_tv.getText().toString();// 当前节点
        if (folderPath.equals(mSelectorRootPathName)) {
            this.finish();
        } else if (isFileOnClickShowOk) {
            folderPath = SdCardUtil.replaceLocalNameWithAbsPath(folderPath);
            if (rootPaths.contains(folderPath)) {
                mFolderPath_tv.setText(mSelectorRootPathName);
                refreshByParentPath(mSelectorRootPathName);
            } else {
                String parentPath = new File(folderPath).getParentFile().getAbsolutePath();// 返回上一级的节点
                refreshByParentPath(parentPath);
                mFolderPath_tv.setText(SdCardUtil.replaceAbsPathWithLocalName(parentPath));
            }
        }
    }

    public String getSelectorFileTitle() {
        return mSelectorFileTitle;
    }

    public void setSelectorFileTitle(String mSelectorFileTitle) {
        this.mSelectorFileTitle = mSelectorFileTitle;
    }

    public String getSelectorFolderTitle() {
        return mSelectorFolderTitle;
    }

    public void setSelectorFolderTitle(String mSelectorFolderTitle) {
        this.mSelectorFolderTitle = mSelectorFolderTitle;
    }

    public String getSelectorRootPathName() {
        return mSelectorRootPathName;
    }

    public void setSelectorRootPathName(String mSelectorRootPathName) {
        this.mSelectorRootPathName = mSelectorRootPathName;
    }

    public int getSelectorMode() {
        return mSelectorMode;
    }

    public void setSelectorMode(int mSelectorMode) {
        this.mSelectorMode = mSelectorMode;
    }

    public boolean isSelectorIsMultiple() {
        return mSelectorIsMultiple;
    }

    public void setSelectorIsMultiple(boolean mSelectorIsMultiple) {
        this.mSelectorIsMultiple = mSelectorIsMultiple;
    }

    public int getSelectorFolderIcon() {
        return mSelectorFolderIcon;
    }

    public void setSelectorFolderIcon(int mSelectorFolderIcon) {
        this.mSelectorFolderIcon = mSelectorFolderIcon;
    }

    public int getSelectorFileIcon() {
        return mSelectorFileIcon;
    }

    public void setSelectorFileIcon(int mSelectorFileIcon) {
        this.mSelectorFileIcon = mSelectorFileIcon;
    }

    public int getSelectorIconWidth() {
        return mSelectorIconWidth;
    }

    public void setSelectorIconWidth(int mSelectorIconWidth) {
        this.mSelectorIconWidth = mSelectorIconWidth;
    }

    public int getSelectorIconHeight() {
        return mSelectorIconHeight;
    }

    public void setSelectorIconHeight(int mSelectorIconHeight) {
        this.mSelectorIconHeight = mSelectorIconHeight;
    }

    /**
     * 根据
     *
     * @param pattern      类似于"yyyy-MM-dd"的格式
     * @param milliseconds 毫秒数（measured in milliseconds since January 1st, 1970,
     *                     midnight. ）
     * @return
     */
    public String getLocalDateByMilliseconds(long milliseconds, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        String strDate = new SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.getTime());
        return strDate;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fileselector_ok:
                onClickOkBtn();
                break;
            case R.id.img_title_back:
                this.finish();
                break;
        }

    }
}