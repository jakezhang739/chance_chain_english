package com.example.jake.chance_chain;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "chance-mobilehub-653619147-UserPool")

public class UserPoolDO {
    private String _userId;
    private String _career;
    private String _chanceId;
    private List<String> _chanceIdList;
    private String _gender;
    private String _name;
    private String _nickName;
    private String _resume;
    private String _walletAddress;
    private Double _availableWallet=0.0;
    private List<String> _beiGuanZhu;
    private Double _candyCurrency=0.0;
    private Double _consecutiveLogin=0.0;
    private Double _cryptoCurrency=0.0;
    private Double _frozenwallet=0.0;
    private List<String> _gottenList;
    private List<String> _guanZhu;
    private String _lastComfirm;
    private String _lastFabu;
    private String _lastGet;
    private String _lastLogin;
    private String _lastZhuan;
    private String _myEmail;
    private String _profilePic;
    private Double _shengWang=0.0;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "Career")
    public String getCareer() {
        return _career;
    }

    public void setCareer(final String _career) {
        this._career = _career;
    }
    @DynamoDBAttribute(attributeName = "ChanceId")
    public String getChanceId() {
        return _chanceId;
    }

    public void setChanceId(final String _chanceId) {
        this._chanceId = _chanceId;
    }
    @DynamoDBAttribute(attributeName = "ChanceIdList")
    public List<String> getChanceIdList() {
        return _chanceIdList;
    }

    public void setChanceIdList(final List<String> _chanceIdList) {
        this._chanceIdList = _chanceIdList;
    }
    @DynamoDBAttribute(attributeName = "Gender")
    public String getGender() {
        return _gender;
    }

    public void setGender(final String _gender) {
        this._gender = _gender;
    }
    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "NickName")
    public String getNickName() {
        return _nickName;
    }

    public void setNickName(final String _nickName) {
        this._nickName = _nickName;
    }
    @DynamoDBAttribute(attributeName = "Resume")
    public String getResume() {
        return _resume;
    }

    public void setResume(final String _resume) {
        this._resume = _resume;
    }
    @DynamoDBAttribute(attributeName = "WalletAddress")
    public String getWalletAddress() {
        return _walletAddress;
    }

    public void setWalletAddress(final String _walletAddress) {
        this._walletAddress = _walletAddress;
    }
    @DynamoDBAttribute(attributeName = "availableWallet")
    public Double getAvailableWallet() {
        return _availableWallet;
    }

    public void setAvailableWallet(final Double _availableWallet) {
        this._availableWallet = _availableWallet;
    }
    @DynamoDBAttribute(attributeName = "beiGuanZhu")
    public List<String> getBeiGuanZhu() {
        return _beiGuanZhu;
    }

    public void setBeiGuanZhu(final List<String> _beiGuanZhu) {
        this._beiGuanZhu = _beiGuanZhu;
    }
    @DynamoDBAttribute(attributeName = "candyCurrency")
    public Double getCandyCurrency() {
        return _candyCurrency;
    }

    public void setCandyCurrency(final Double _candyCurrency) {
        this._candyCurrency = _candyCurrency;
    }
    @DynamoDBAttribute(attributeName = "consecutiveLogin")
    public Double getConsecutiveLogin() {
        return _consecutiveLogin;
    }

    public void setConsecutiveLogin(final Double _consecutiveLogin) {
        this._consecutiveLogin = _consecutiveLogin;
    }
    @DynamoDBAttribute(attributeName = "cryptoCurrency")
    public Double getCryptoCurrency() {
        return _cryptoCurrency;
    }

    public void setCryptoCurrency(final Double _cryptoCurrency) {
        this._cryptoCurrency = _cryptoCurrency;
    }
    @DynamoDBAttribute(attributeName = "frozenwallet")
    public Double getFrozenwallet() {
        return _frozenwallet;
    }

    public void setFrozenwallet(final Double _frozenwallet) {
        this._frozenwallet = _frozenwallet;
    }
    @DynamoDBAttribute(attributeName = "gottenList")
    public List<String> getGottenList() {
        return _gottenList;
    }

    public void setGottenList(final List<String> _gottenList) {
        this._gottenList = _gottenList;
    }
    @DynamoDBAttribute(attributeName = "guanZhu")
    public List<String> getGuanZhu() {
        return _guanZhu;
    }

    public void setGuanZhu(final List<String> _guanZhu) {
        this._guanZhu = _guanZhu;
    }
    @DynamoDBAttribute(attributeName = "lastComfirm")
    public String getLastComfirm() {
        return _lastComfirm;
    }

    public void setLastComfirm(final String _lastComfirm) {
        this._lastComfirm = _lastComfirm;
    }
    @DynamoDBAttribute(attributeName = "lastFabu")
    public String getLastFabu() {
        return _lastFabu;
    }

    public void setLastFabu(final String _lastFabu) {
        this._lastFabu = _lastFabu;
    }
    @DynamoDBAttribute(attributeName = "lastGet")
    public String getLastGet() {
        return _lastGet;
    }

    public void setLastGet(final String _lastGet) {
        this._lastGet = _lastGet;
    }
    @DynamoDBAttribute(attributeName = "lastLogin")
    public String getLastLogin() {
        return _lastLogin;
    }

    public void setLastLogin(final String _lastLogin) {
        this._lastLogin = _lastLogin;
    }
    @DynamoDBAttribute(attributeName = "lastZhuan")
    public String getLastZhuan() {
        return _lastZhuan;
    }

    public void setLastZhuan(final String _lastZhuan) {
        this._lastZhuan = _lastZhuan;
    }
    @DynamoDBIndexHashKey(attributeName = "myEmail", globalSecondaryIndexName = "GetStuff")
    public String getMyEmail() {
        return _myEmail;
    }

    public void setMyEmail(final String _myEmail) {
        this._myEmail = _myEmail;
    }
    @DynamoDBAttribute(attributeName = "profilePic")
    public String getProfilePic() {
        return _profilePic;
    }

    public void setProfilePic(final String _profilePic) {
        this._profilePic = _profilePic;
    }
    @DynamoDBAttribute(attributeName = "shengWang")
    public Double getShengWang() {
        return _shengWang;
    }

    public void setShengWang(final Double _shengWang) {
        this._shengWang = _shengWang;
    }

}