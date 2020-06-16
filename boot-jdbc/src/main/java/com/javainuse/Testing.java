package com.javainuse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

public class Testing {

 

    public static void main(String[] args) {
        //String toLog = "GuiServiceLoggingProxy: " + name + " invoked with " + args + " returning " + toClaimAsReturn;
        //String toLog ="GuiServiceLoggingProxy: validatePassword invoked with netapp1! returning true";
        String toLog ="'EXSI host 1.2.3.9 does not support the following file system versio: FC/FCoE','EXSI host 4.5.6.1 does not support the following file system versio: FC/FCoE','EXSI host 7.8.9.5 does not support the following file system versio: FC/FCoE'";
        String abc = formatErroMessage(toLog);
        System.out.println(abc);
    }

 
    
    public static String formatErroMessage(String errorMessage) {
	    Pattern pattern = Pattern.compile("host(.*?)does");
	    String[] first = errorMessage.split(",");
	    Matcher matcher = pattern.matcher(errorMessage);
	    StringBuffer sb = new StringBuffer();
	    while (matcher.find()) {
	        sb.append(matcher.group(1)).append(",");
	    }
	    return first[0].replaceAll("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}+", sb.toString()).replaceAll("(\\d)(?=(\\d{3})+$)","$1, ");
	}

    static List<Pattern> patterns = toList(getPattern(".*<Password>[^*]*</Password>.*"),
            getPattern(".*<Username>[^*]*</Username>.*"), getPattern(".*-password=(\\S*).*"),
            getPattern(".*-password%3d(\\S*).*"), getPattern(".*<filerPassword>(\\S*)(</filerPassword>.*)"),
            getPattern("<([^<]*):password>([^<]*)</([^<]*):password>"),
            getPattern(".*<vCenterPassword.*>(\\S*)</vCenterPassword>.*"), getPattern(".*<password>[^*]*</password>.*"),
            getPattern(".*<[^<]*password>[^*]*</.*password>.*"), getPattern(".*<VPPassword.*>(\\S*)</VPPassword>.*"),
            getPattern(".*&password=(\\S*).*"),
            getPattern("(.*RemoteInvocation.*registerVasaProviderWithVscAndPassword.*)"),
            getPattern("Password.*Password"), getPattern("(.*RemoteInvocation.*unregisterVasaProviderWithPassword.*)"),
            (getPattern(".*<password>[^*]*</password>.*")),
            getPattern("validatePassword.*"), getPattern("getVasaProviderPassword.*")
            , getPattern("(.*RemoteInvocation.*validatePassword.*)")
            , getPattern("(.*RemoteInvocation.*toggleCapabilities.*)")
            );

 

    public static <T> List<T> toList(T... objects) {
        ArrayList<T> retVal = new ArrayList<T>();
        if (objects != null) {
            for (T o : objects) {
                retVal.add(o);
            }
        }
        return retVal;
    }

 

    static List<String> replacements = toList("<Password>***</Password>", "<Username>***</Username>",
            "password_removed ", "-********", "<password_removed/>", "$1********$3", "<vCenterPasswordRemoved/>",
            "<passwordRemoved/>", "<passwordRemoved/>", "<VPPasswordRemoved/>", "password_removed",
            "remote_invocation_password_removed", "password_removed", "remote_invocation_password_removed",
            "password_removed","password_removed","password_removed","password_removed","password_removed");

 

    private static Pattern getPattern(String pattern) {
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

 

    public static String removePassword(String incoming) {
        // only need to scrape this stuff is a possible password string is present
        if (incoming != null && incoming.contains("assword")) {
            for (int i = 0; i < patterns.size(); i++) {
                Pattern p = patterns.get(i);
                Matcher m = p.matcher(incoming);
                if (m.matches()) {
                    String replacementTarget = patterns.get(i).pattern();
                    replacementTarget = replacementTarget.trim();
                    if (replacementTarget.startsWith(".*")) {
                        replacementTarget = replacementTarget.substring(2);
                    }
                    if (replacementTarget.endsWith(".*")) {
                        replacementTarget = replacementTarget.substring(0, replacementTarget.length() - 2);
                    }
                    Pattern pReplacementTarget = getPattern(replacementTarget);

 

                    incoming = pReplacementTarget.matcher(incoming).replaceAll(replacements.get(i));
                    m = patterns.get(i).matcher(incoming);
                    if (m.matches()) {
                        // still matches, lets just blast this part of the string
                        incoming = "password fragment area removed";
                    }
                } else {
                    Pattern passPattern = Pattern.compile(patterns.get(i).pattern().trim());
                    Matcher passMatcher = passPattern.matcher(incoming);
                    while (passMatcher.find()) {
                        incoming = passMatcher.replaceAll(replacements.get(i));
                    }
                }

 

            }
        }
        return incoming;
    }
}
