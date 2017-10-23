package cn.xinxizhan.test.tdemo.data.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.utils.HttpHelper;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by admin on 2017/10/10.
 */

public class User {
    private String username;
    private String password;
    private String xzqhdm;
    private String aliasname;
    private String userInfoLink;
    private String branchInfoLink;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setXzqhdm(String xzqhdm) {
        this.xzqhdm = xzqhdm;
    }

    public void setAliasname(String aliasname) {
        this.aliasname = aliasname;
    }

    public void setUserInfoLink(String userInfoLink) {
        this.userInfoLink = userInfoLink;
    }

    public void setBranchInfoLink(String branchInfoLink) {
        this.branchInfoLink = branchInfoLink;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getXzqhdm() {
        return xzqhdm;
    }

    public String getAliasname() {
        return aliasname;
    }

    public String getUserInfoLink() {
        return userInfoLink;
    }

    public String getBranchInfoLink() {
        return branchInfoLink;
    }

    public static Observable<User> login(String url, final User user){
        HashMap<String,String> map = new HashMap<>();
        map.put("loginname",user.username);
        map.put("password",user.password);
        return HttpHelper.postValues(url,map).map(new Function<String, User>() {
            @Override
            public User apply(@NonNull String s) throws Exception {
                JSONObject result= null;
                result = new JSONObject(s);
                String code = result.getString("code");
                if(code.equals("200")){
                    user.userInfoLink = result.getString("link");
                    return user;
                }
                else {
                    return null;
                }

            }
        });
    }

    public static Observable<User> getUserInfo(String url,final User user){
        return HttpHelper.getString(url).map(new Function<String, User>() {
            @Override
            public User apply(@NonNull String s) throws Exception {
                JSONObject result = null;
                result = new JSONObject(s);
                user.branchInfoLink = ApplicationConstants.HOSTURL + result.getJSONObject("branch").getString("link");
                user.aliasname = result.getString("aliasname");
                return user;
            }
        });
    }

    public static Observable<User> getBranchInfo(String url,final User user){
        return HttpHelper.getString(url).map(new Function<String, User>() {
            @Override
            public User apply(@NonNull String s) throws Exception{
                JSONObject result= null;
                result = new JSONObject(s);
                user.xzqhdm =  result.getJSONObject("region").getString("regioncode");
                return user;
            }
        });
    }

    public static String string(User user){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",user.getUsername());
            jsonObject.put("password",user.getPassword());
            jsonObject.put("aliasname",user.getAliasname());
            jsonObject.put("xzqhdm",user.getXzqhdm());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static User fromString(String s){
        if (s==null || s=="")
            return null;
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            if(object!=null){
                User user = new User(object.getString("username"),object.getString("password"));
                user.setAliasname(object.getString("aliasname"));
                user.setXzqhdm(object.getString("xzqhdm"));
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User)
        {
            return this.getUsername().equals(((User)obj).getUsername());
        }
        return false;
    }
}
