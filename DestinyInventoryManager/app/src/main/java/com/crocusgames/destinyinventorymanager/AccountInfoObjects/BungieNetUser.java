
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BungieNetUser {

    @SerializedName("membershipId")
    @Expose
    private String membershipId;
    @SerializedName("uniqueName")
    @Expose
    private String uniqueName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("profilePicture")
    @Expose
    private Long profilePicture;
    @SerializedName("profileTheme")
    @Expose
    private Long profileTheme;
    @SerializedName("userTitle")
    @Expose
    private Long userTitle;
    @SerializedName("successMessageFlags")
    @Expose
    private String successMessageFlags;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("firstAccess")
    @Expose
    private String firstAccess;
    @SerializedName("lastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("context")
    @Expose
    private Context context;
    @SerializedName("psnDisplayName")
    @Expose
    private String psnDisplayName;
    @SerializedName("showActivity")
    @Expose
    private Boolean showActivity;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("localeInheritDefault")
    @Expose
    private Boolean localeInheritDefault;
    @SerializedName("showGroupMessaging")
    @Expose
    private Boolean showGroupMessaging;
    @SerializedName("profilePicturePath")
    @Expose
    private String profilePicturePath;
    @SerializedName("profilePictureWidePath")
    @Expose
    private String profilePictureWidePath;
    @SerializedName("profileThemeName")
    @Expose
    private String profileThemeName;
    @SerializedName("userTitleDisplay")
    @Expose
    private String userTitleDisplay;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("statusDate")
    @Expose
    private String statusDate;

    /**
     * 
     * @return
     *     The membershipId
     */
    public String getMembershipId() {
        return membershipId;
    }

    /**
     * 
     * @param membershipId
     *     The membershipId
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * 
     * @return
     *     The uniqueName
     */
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * 
     * @param uniqueName
     *     The uniqueName
     */
    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The profilePicture
     */
    public Long getProfilePicture() {
        return profilePicture;
    }

    /**
     * 
     * @param profilePicture
     *     The profilePicture
     */
    public void setProfilePicture(Long profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * 
     * @return
     *     The profileTheme
     */
    public Long getProfileTheme() {
        return profileTheme;
    }

    /**
     * 
     * @param profileTheme
     *     The profileTheme
     */
    public void setProfileTheme(Long profileTheme) {
        this.profileTheme = profileTheme;
    }

    /**
     * 
     * @return
     *     The userTitle
     */
    public Long getUserTitle() {
        return userTitle;
    }

    /**
     * 
     * @param userTitle
     *     The userTitle
     */
    public void setUserTitle(Long userTitle) {
        this.userTitle = userTitle;
    }

    /**
     * 
     * @return
     *     The successMessageFlags
     */
    public String getSuccessMessageFlags() {
        return successMessageFlags;
    }

    /**
     * 
     * @param successMessageFlags
     *     The successMessageFlags
     */
    public void setSuccessMessageFlags(String successMessageFlags) {
        this.successMessageFlags = successMessageFlags;
    }

    /**
     * 
     * @return
     *     The isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 
     * @param isDeleted
     *     The isDeleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 
     * @return
     *     The about
     */
    public String getAbout() {
        return about;
    }

    /**
     * 
     * @param about
     *     The about
     */
    public void setAbout(String about) {
        this.about = about;
    }

    /**
     * 
     * @return
     *     The firstAccess
     */
    public String getFirstAccess() {
        return firstAccess;
    }

    /**
     * 
     * @param firstAccess
     *     The firstAccess
     */
    public void setFirstAccess(String firstAccess) {
        this.firstAccess = firstAccess;
    }

    /**
     * 
     * @return
     *     The lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * 
     * @param lastUpdate
     *     The lastUpdate
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * 
     * @return
     *     The context
     */
    public Context getContext() {
        return context;
    }

    /**
     * 
     * @param context
     *     The context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 
     * @return
     *     The psnDisplayName
     */
    public String getPsnDisplayName() {
        return psnDisplayName;
    }

    /**
     * 
     * @param psnDisplayName
     *     The psnDisplayName
     */
    public void setPsnDisplayName(String psnDisplayName) {
        this.psnDisplayName = psnDisplayName;
    }

    /**
     * 
     * @return
     *     The showActivity
     */
    public Boolean getShowActivity() {
        return showActivity;
    }

    /**
     * 
     * @param showActivity
     *     The showActivity
     */
    public void setShowActivity(Boolean showActivity) {
        this.showActivity = showActivity;
    }

    /**
     * 
     * @return
     *     The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * 
     * @param locale
     *     The locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * 
     * @return
     *     The localeInheritDefault
     */
    public Boolean getLocaleInheritDefault() {
        return localeInheritDefault;
    }

    /**
     * 
     * @param localeInheritDefault
     *     The localeInheritDefault
     */
    public void setLocaleInheritDefault(Boolean localeInheritDefault) {
        this.localeInheritDefault = localeInheritDefault;
    }

    /**
     * 
     * @return
     *     The showGroupMessaging
     */
    public Boolean getShowGroupMessaging() {
        return showGroupMessaging;
    }

    /**
     * 
     * @param showGroupMessaging
     *     The showGroupMessaging
     */
    public void setShowGroupMessaging(Boolean showGroupMessaging) {
        this.showGroupMessaging = showGroupMessaging;
    }

    /**
     * 
     * @return
     *     The profilePicturePath
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * 
     * @param profilePicturePath
     *     The profilePicturePath
     */
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * 
     * @return
     *     The profilePictureWidePath
     */
    public String getProfilePictureWidePath() {
        return profilePictureWidePath;
    }

    /**
     * 
     * @param profilePictureWidePath
     *     The profilePictureWidePath
     */
    public void setProfilePictureWidePath(String profilePictureWidePath) {
        this.profilePictureWidePath = profilePictureWidePath;
    }

    /**
     * 
     * @return
     *     The profileThemeName
     */
    public String getProfileThemeName() {
        return profileThemeName;
    }

    /**
     * 
     * @param profileThemeName
     *     The profileThemeName
     */
    public void setProfileThemeName(String profileThemeName) {
        this.profileThemeName = profileThemeName;
    }

    /**
     * 
     * @return
     *     The userTitleDisplay
     */
    public String getUserTitleDisplay() {
        return userTitleDisplay;
    }

    /**
     * 
     * @param userTitleDisplay
     *     The userTitleDisplay
     */
    public void setUserTitleDisplay(String userTitleDisplay) {
        this.userTitleDisplay = userTitleDisplay;
    }

    /**
     * 
     * @return
     *     The statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * 
     * @param statusText
     *     The statusText
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    /**
     * 
     * @return
     *     The statusDate
     */
    public String getStatusDate() {
        return statusDate;
    }

    /**
     * 
     * @param statusDate
     *     The statusDate
     */
    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

}
