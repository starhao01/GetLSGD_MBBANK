package com.viettelpost.core.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VietnameseUtils {
    private static char[] VOWELS_LOWERCASE = "aeiouy".toCharArray();
    private static char[] SOURCE_CHARACTERS = "ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ".toCharArray();
    private static char[] DESTINATION_CHARACTERS_LOWERCASE = "aaaaeeeiioooouuyaaaaeeeiioooouuyaaddiiuuoouuaaaaaaaaaaaaaaaaaaaaaaaaeeeeeeeeeeeeeeeeiiiioooooooooooooooooooooooouuuuuuuuuuuuuuyyyyyyyy".toCharArray();
    private static char[] DESTINATION_CHARACTERS = "AAAAEEEIIOOOOUUYaaaaeeeiioooouuyAaDdIiUuOoUuAaAaAaAaAaAaAaAaAaAaAaAaEeEeEeEeEeEeEeEeIiIiOoOoOoOoOoOoOoOoOoOoOoOoUuUuUuUuUuUuUuYyYyYyYy".toCharArray();
    private static char[] SOURCE_ACCENT_MARKS_CHARACTERS = "AEIOUYaeiouyÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ".toCharArray();
    private static char[] DESTINATION_ACCENT_MARKS_CHARACTERS = "AEIOUYaeiouyAAÂAEEÊIIOOÔOUUYaaâaeeêiiooôouuyĂăIiUuƠơƯưAaAaÂâÂâÂâÂâÂâĂăĂăĂăĂăĂăEeEeEeÊêÊêÊêÊêÊêIiIiOoOoÔôÔôÔôÔôÔôƠơƠơƠơƠơƠơUuUuƯưƯưƯưƯưƯưYyYyYyYy".toCharArray();
    private static char[] DESTINATION_ACCENT_MARKS = "000000000000210421021210421121042102121042110044440000553311223344551122334455553344112233445533555533112233445511223344555533112233445522553344".toCharArray();


    private final static Pattern MISSING_SPACE = Pattern.compile("(?:([A-Z" + vietnameseUppercaseCharacters() + "][a-z" + vietnameseLowercaseCharacters() + "]+)[A-Z" + vietnameseUppercaseCharacters() + "][a-z" + vietnameseLowercaseCharacters() + "])"
            + "|(\\d+)[A-Za-z" + new String(SOURCE_CHARACTERS) + "]");

    private VietnameseUtils() {
    }

    //Pattern TONEPOSITION = Pattern.compile("o(á|à|ả|ã|ạ)") ấ
    private static int toLowcaseInc = 'a' - 'A';

    public static String vietnameseUppercaseCharacters() {
        StringBuilder sb = new StringBuilder();
        for (char ch : SOURCE_CHARACTERS) {
            if (Character.isUpperCase(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static boolean isVietnameseCharacter(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
            return true;
        }
        return Arrays.binarySearch(SOURCE_CHARACTERS, ch) >= 0;
    }

    public static String vietnameseLowercaseCharacters(char ch) {
        StringBuilder sb = new StringBuilder().append(ch);
        for (int i = 0; i < SOURCE_CHARACTERS.length; i++) {
            if (DESTINATION_CHARACTERS[i] == ch) {
                sb.append(SOURCE_CHARACTERS[i]);
            }
        }
        return sb.toString();
    }

    public static String vietnameseLowercaseCharacters() {
        StringBuilder sb = new StringBuilder();
        for (char ch : SOURCE_CHARACTERS) {
            if (Character.isLowerCase(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static boolean isVietnamese(String text) {
        for (int i = text.length() - 1; i >= 0; i--) {
            char ch = text.charAt(i);
            if (ch < 128) {
                continue;
            }
            if (Arrays.binarySearch(SOURCE_CHARACTERS, ch) >= 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isANSI(String text) {
        return isANSI(text, 0, text.length());
    }

    public static boolean isANSI(List<String> tokens) {
        return tokens.stream().allMatch(VietnameseUtils::isANSI);
    }

    public static boolean isANSI(String text, int start, int end) {
        while (start < end) {
            if (text.charAt(start) > 127) {
                return false;
            }
            start++;
        }
        return true;
    }

    public static boolean hasVowel(String text) {
        int start = 0;
        int end = text.length();
        while (start < end) {
            int index = Arrays.binarySearch(VOWELS_LOWERCASE, text.charAt(start));
            if (index >= 0) {
                return true;
            }
            start++;
        }
        return false;
    }

    public static boolean hasTone(String text, int start, int end) {
        while (start < end) {
            int index = Arrays.binarySearch(SOURCE_CHARACTERS, text.charAt(start));
            if (index >= 0) {
                return true;
            }
            start++;
        }
        return false;
    }

    public static boolean hasTone(char[] text, int start, int end) {
        while (start < end) {
            int index = Arrays.binarySearch(SOURCE_CHARACTERS, text[start]);
            if (index >= 0) {
                return true;
            }
            start++;
        }
        return false;
    }

    public static String removeAccentAndReplace(String s, Pattern p) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += toLowcaseInc;
                sb.setCharAt(i, ch);
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    sb.setCharAt(i, DESTINATION_CHARACTERS[index]);
                }
            }
        }
        Matcher m = p.matcher(sb);
        while (m.find()) {
            for (int i = m.start(); i < m.end(); i++) {
                sb.setCharAt(i, ' ');
            }
        }
        return sb.toString();
    }

    public static char[] removeAccent(char[] chars, int start, int end) {
        char[] noneAccent = new char[end - start];
        for (int idx = 0; start < end; start++, idx++) {
            char ch = chars[start];
            if (ch >= 'A' && ch <= 'Z') {
                ch += toLowcaseInc;
                noneAccent[idx] = ch;
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    noneAccent[idx] = DESTINATION_CHARACTERS[index];
                }
            } else {
                noneAccent[idx] = ch;
            }
        }
        return noneAccent;
    }

    public static char removeAccent(char ch) {
        if (ch > 127) {
            int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
            if (index >= 0) {
                return DESTINATION_CHARACTERS[index];
            }
        }
        return ch;
    }

    public static void removeAccent(char[] chars) {
        for (int idx = 0; idx < chars.length; idx++) {
            char ch = chars[idx];
            if (ch >= 'A' && ch <= 'Z') {
                ch += toLowcaseInc;
                chars[idx] = ch;
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    chars[idx] = DESTINATION_CHARACTERS[index];
                }
            }
        }
    }

    public static boolean startsWith(String text, String prefix) {
        if (text.length() < prefix.length()) {
            return false;
        }
        int length = Math.min(prefix.length(), text.length());
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += toLowcaseInc;
                if (prefix.charAt(i) != ch) {
                    return false;
                }
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    if (prefix.charAt(i) != DESTINATION_CHARACTERS[index]) {
                        return false;
                    }
                }
            } else if (prefix.charAt(i) != ch) {
                return false;
            }
        }
        return true;
    }

    public static String removeAccent(String s) {
        if (s == null) return null;
        char[] noneAccent = s.toCharArray();
        int start = 0;
        for (int i = 0; i < noneAccent.length; i++) {
            char ch = noneAccent[i];
            if (start == i && ch <= ' ') {
                start++;
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    noneAccent[i] = DESTINATION_CHARACTERS[index];
                }
            }
        }
        return new String(noneAccent, start, noneAccent.length - start);
    }

    public static List<String> removeAccent(List<String> token) {
        return token.stream().map(VietnameseUtils::removeAccentWithoutTrim).collect(Collectors.toList());
    }

    public static List<String> removeAccent(String[] token) {
        List<String> rs = new ArrayList<>(token.length);
        for (int i = 0; i < token.length; i++) {
            rs.add(removeAccentWithoutTrim(token[i]));
        }
        return rs;
    }

    public static String repairSpaceMissing(String text) {
        Matcher m = MISSING_SPACE.matcher(text);
        if (m.find()) {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            do {
                int end = m.group(1) != null ? m.end(1) : m.end(2);
                sb.append(text, start, end).append(' ');
                start = end;
            } while (m.find(start));
            sb.append(text, start, text.length());
            return sb.toString();
        }
        return text;
    }

    public static String removeAccentWithoutTrim(String s) {
        if (s == null) return null;
        char[] noneAccent = s.toCharArray();
        int start = 0;
        for (int i = 0; i < noneAccent.length; i++) {
            char ch = noneAccent[i];
            if (start == i && ch <= ' ') {
                start++;
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    noneAccent[i] = DESTINATION_CHARACTERS[index];
                }
            }
        }
        return new String(noneAccent);
    }

    public static String removeAccentAndLowercase(String s) {
        if (s == null) return null;
        char[] noneAccent = s.toCharArray();
        int start = 0;
        for (int i = 0; i < noneAccent.length; i++) {
            char ch = noneAccent[i];
            if (start == i && ch <= ' ') {
                start++;
            } else if (ch >= 'A' && ch <= 'Z') {
                ch += toLowcaseInc;
                noneAccent[i] = ch;
            } else if (ch > 127) {
                int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
                if (index >= 0) {
                    noneAccent[i] = DESTINATION_CHARACTERS_LOWERCASE[index];
                }
            }
        }
        return new String(noneAccent, start, noneAccent.length - start);
    }

    public static String acronym(String name) {
        if (name == null) return null;
        char[] chars = name.toCharArray();
        removeAccent(chars);
        StringBuilder sb = new StringBuilder(3);
        boolean space = true;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch <= ' ') {
                space = true;
            } else if (((ch > 'a' || ch <= 'z') || (ch > 'A' && ch <= 'Z')) && space) {
                sb.append(ch);
                space = false;
            }
        }
        return sb.length() > 1 ? sb.toString() : null;
    }

    private final static Pattern OLD_TONE_POS = Pattern.compile("((?:[ÓÒỎÕỌÚÙỦŨỤóòỏõọúùủũụ](?:[Aa][CMNTcmnt]|[ĂÂEÊOƠÔYăâeêoơôy]))|(?:[ÍÌỈĨỊíìỉĩị][Êê][cmnu]))|(?:[^qQ]([uưUƯ][áàảãạíìỉĩịÁÀẢÃẠÍÌỈĨỊ])(?:$|[^CMNUTcmnut]))|([ýỳỷỹỵÝỲỶỸỴ][êÊ])");


    private final static char[] VOWEL_A = "ÁÀẢÃẠ".toCharArray();
    private final static char[] VOWEL_AW = "ẮẰẲẴẶ".toCharArray();
    private final static char[] VOWEL_AA = "ẤẦẨẪẬ".toCharArray();
    private final static char[] VOWEL_E = "ÉÈẺẼẸ".toCharArray();
    private final static char[] VOWEL_EE = "ẾỀỂỄỆ".toCharArray();
    private final static char[] VOWEL_O = "ÓÒỎÕỌ".toCharArray();
    private final static char[] VOWEL_OW = "ỚỜỞỠỢ".toCharArray();
    private final static char[] VOWEL_OO = "ỐỒỔỖỘ".toCharArray();
    private final static char[] VOWEL_Y = "ÝỲỶỸỴ".toCharArray();
    private final static char[] VOWEL_U = "ÚÙỦŨỤ".toCharArray();
    private final static char[] VOWEL_UW = "ỨỪỬỮỰ".toCharArray();
    private final static char[] VOWEL_I = "ÍÌỈĨỊ".toCharArray();


    private final static char[] VOWEL_a = "áàảãạ".toCharArray();
    private final static char[] VOWEL_aw = "ắằẳẵặ".toCharArray();
    private final static char[] VOWEL_aa = "ấầẩẫậ".toCharArray();
    private final static char[] VOWEL_e = "éèẻẽẹ".toCharArray();
    private final static char[] VOWEL_ee = "ếềểễệ".toCharArray();
    private final static char[] VOWEL_o = "óòỏõọ".toCharArray();
    private final static char[] VOWEL_ow = "ớờởỡợ".toCharArray();
    private final static char[] VOWEL_oo = "ốồổỗộ".toCharArray();
    private final static char[] VOWEL_y = "ýỳỷỹỵ".toCharArray();
    private final static char[] VOWEL_u = "úùủũụ".toCharArray();
    private final static char[] VOWEL_uw = "ứừửữự".toCharArray();
    private final static char[] VOWEL_i = "íìỉĩị".toCharArray();

    private static char setAccentMark(char vowel, char mark) {
        char[] vowelWithAccentMark;
        switch (vowel) {
            case 'A':
                vowelWithAccentMark = VOWEL_A;
                break;
            case 'a':
                vowelWithAccentMark = VOWEL_a;
                break;
            case 'Ă':
                vowelWithAccentMark = VOWEL_AW;
                break;
            case 'ă':
                vowelWithAccentMark = VOWEL_aw;
                break;
            case 'Â':
                vowelWithAccentMark = VOWEL_AA;
                break;
            case 'â':
                vowelWithAccentMark = VOWEL_aa;
                break;
            case 'E':
                vowelWithAccentMark = VOWEL_E;
                break;
            case 'e':
                vowelWithAccentMark = VOWEL_e;
                break;
            case 'Ê':
                vowelWithAccentMark = VOWEL_EE;
                break;
            case 'ê':
                vowelWithAccentMark = VOWEL_ee;
                break;
            case 'I':
                vowelWithAccentMark = VOWEL_I;
                break;
            case 'i':
                vowelWithAccentMark = VOWEL_i;
                break;
            case 'O':
                vowelWithAccentMark = VOWEL_O;
                break;
            case 'o':
                vowelWithAccentMark = VOWEL_o;
                break;
            case 'Ô':
                vowelWithAccentMark = VOWEL_OO;
                break;
            case 'ô':
                vowelWithAccentMark = VOWEL_oo;
                break;
            case 'Ơ':
                vowelWithAccentMark = VOWEL_OW;
                break;
            case 'ơ':
                vowelWithAccentMark = VOWEL_ow;
                break;
            case 'U':
                vowelWithAccentMark = VOWEL_U;
                break;
            case 'u':
                vowelWithAccentMark = VOWEL_u;
                break;
            case 'Ư':
                vowelWithAccentMark = VOWEL_UW;
                break;
            case 'ư':
                vowelWithAccentMark = VOWEL_uw;
                break;
            case 'Y':
                vowelWithAccentMark = VOWEL_Y;
                break;
            case 'y':
                vowelWithAccentMark = VOWEL_y;
                break;
            default:
                return vowel;
        }
        return vowelWithAccentMark[mark - '1'];
    }

    private static void replaceMark(char[] buff, int buffSize, char accentMark) {
        int lastIndex = buffSize - 1;
        int index = lastIndex;
        char lastVowel = buff[index];
        index--;
        char prevVowel = buff[index];
        index--;
        if (index >= 0 && (buff[index] == 'G' || buff[index] == 'g')
                && (prevVowel == 'I' || prevVowel == 'i')
                && (lastVowel == 'A' || lastVowel == 'a' || lastVowel == 'Ă' || lastVowel == 'ă' | lastVowel == 'Â' || lastVowel == 'â')) {
            buff[lastIndex] = setAccentMark(buff[lastIndex], accentMark);
            return;
        }
        switch (lastVowel) {
            case 'A':
            case 'Ă':
                //*ia=ia|iâ=ia|iă=ia|
                if (prevVowel == 'I' || prevVowel == 'i') {
                    buff[lastIndex] = 'A';
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                if ((prevVowel == 'U' || prevVowel == 'u') && (index < 0 || (buff[index] != 'Q' && buff[index] != 'q'))) {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                break;
            case 'ă':
            case 'a':
                if (prevVowel == 'I' || prevVowel == 'i') {
                    buff[lastIndex] = 'a';
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                if ((prevVowel == 'U' || prevVowel == 'u') && (index < 0 || (buff[index] != 'Q' && buff[index] != 'q'))) {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                break;
            case 'O':
            case 'o':
                //*ao=ao|ăo=ao|âo=ao|
                if (prevVowel == 'A' || prevVowel == 'Ă' || prevVowel == 'Â') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('A', accentMark);
                    return;
                }
                if (prevVowel == 'a' || prevVowel == 'ă' || prevVowel == 'â') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('a', accentMark);
                    return;
                }
                //*eo=eo|êo=eo|
                if (prevVowel == 'E' || prevVowel == 'Ê') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('E', accentMark);
                    return;
                }
                if (prevVowel == 'e' || prevVowel == 'ê') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('e', accentMark);
                    return;
                }
                //*ôo=oo|ơo=oo
                if (prevVowel == 'Ô' || prevVowel == 'Ơ') {
                    buff[lastIndex] = setAccentMark(lastVowel, accentMark);
                    lastIndex--;
                    buff[lastIndex] = 'O';
                    return;
                }
                if (prevVowel == 'ô' || prevVowel == 'ơ') {
                    buff[lastIndex] = setAccentMark(lastVowel, accentMark);
                    lastIndex--;
                    buff[lastIndex] = 'O';
                    return;
                }
                break;
            case 'u':
            case 'U':
                //*au=au|ău=au|âu=au
                //eu=êu
                if (prevVowel == 'E' || prevVowel == 'Ê') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('Ê', accentMark);
                    return;
                }
                if (prevVowel == 'e' || prevVowel == 'ê') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('ê', accentMark);
                    return;
                }
                //ơu=ơu|uu=ưu|ưu=ưu
                if (prevVowel == 'A' || prevVowel == 'Ă' || prevVowel == 'Â' || prevVowel == 'a' || prevVowel == 'ă' || prevVowel == 'â' ||
                        prevVowel == 'Ơ' || prevVowel == 'ơ' || prevVowel == 'Ư' || prevVowel == 'ư' || prevVowel == 'Y' || prevVowel == 'y') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                if (prevVowel == 'U') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('Ư', accentMark);
                    return;
                }
                if (prevVowel == 'u') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('ư', accentMark);
                    return;
                }
                break;
            case 'i':
            case 'I':
                //*ai=ai|ăi=ai|âi=ai
                if (prevVowel == 'A' || prevVowel == 'Ă' || prevVowel == 'Â') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('A', accentMark);
                    return;
                }
                if (prevVowel == 'a' || prevVowel == 'ă' || prevVowel == 'â') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('a', accentMark);
                    return;
                }
                //||oi=oi|ơi=ơi|ưi=ưi|ay=ay|ăy=ay|ây=ây|oy=oi|ôy=ôi|ơy=ơi";
                //ơu=ơu|uu=ưu|ưu=ưu
                if (prevVowel == 'O' || prevVowel == 'o' || prevVowel == 'Ô' || prevVowel == 'ô' || prevVowel == 'Ơ' || prevVowel == 'ơ' || prevVowel == 'Ư' || prevVowel == 'ư') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                break;
            case 'y':
            case 'Y':
                if (prevVowel == 'A' || prevVowel == 'Ă') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('A', accentMark);
                    return;
                }
                if (prevVowel == 'a' || prevVowel == 'ă') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark('a', accentMark);
                    return;
                }
                if (prevVowel == 'Â' || prevVowel == 'â') {
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                if (prevVowel == 'O' || prevVowel == 'Ô' || prevVowel == 'Ơ' || prevVowel == 'o' || prevVowel == 'ô' || prevVowel == 'ơ') {
                    buff[lastIndex] = lastVowel == 'Y' ? 'I' : 'i';
                    lastIndex--;
                    buff[lastIndex] = setAccentMark(prevVowel, accentMark);
                    return;
                }
                break;
        }
        buff[lastIndex] = setAccentMark(lastVowel, accentMark);
    }

    public static String normalize(String text) {
        text = Normalizer.normalize(text, Form.NFC).replace('Ð', 'Đ').replace('ð', 'đ').replace('\u00a0', ' ');
        char[] chars = text.toCharArray();
        char mainAccentMark = '0';
        char[] buff = new char[4];
        int buffSize = 0;
        int start = -1;
        int index;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (buffSize < 4 && (index = Arrays.binarySearch(SOURCE_ACCENT_MARKS_CHARACTERS, ch)) >= 0) {
                if (start < 0) {
                    start = i;
                }
                char mark = DESTINATION_ACCENT_MARKS[index];
                char noneMark = DESTINATION_ACCENT_MARKS_CHARACTERS[index];
                if ('0' != mark) {
                    mainAccentMark = mark;
                }
                buff[buffSize] = noneMark;
                buffSize++;
            } else if (buffSize > 0) {
                if (mainAccentMark != '0' && buffSize > 1) {
                    replaceMark(buff, buffSize, mainAccentMark);
                    for (int buffIndex = 0; buffIndex < buffSize; buffIndex++, start++) {
                        chars[start] = buff[buffIndex];
                    }
                }
                mainAccentMark = '0';
                buffSize = 0;
                start = -1;
            }
            if (start < 0 && (ch == 'Q' || ch == 'G' || ch == 'q' || ch == 'g')) {
                start = i;
                buff[buffSize] = ch;
                buffSize++;
            } else if (ch == 'Ð') {
                chars[i] = 'Đ';
            } else if (ch == 'ð') {
                chars[i] = 'đ';
            } else if (ch == '\u00a0') {
                chars[i] = ' ';
            }
        }
        if (buffSize > 1 && mainAccentMark != '0') {
            replaceMark(buff, buffSize, mainAccentMark);
            for (int buffIndex = 0; buffIndex < buffSize; buffIndex++, start++) {
                chars[start] = buff[buffIndex];
            }
        }
        return new String(chars);
    }

    public static String normalize2(String text) {
        text = Normalizer.normalize(text, Form.NFC).replace('Ð', 'Đ').replace('ð', 'đ').replace('\u00a0', ' ');

        Matcher m = OLD_TONE_POS.matcher(text);
        if (m.find()) {
            StringBuilder sb = new StringBuilder(text.length());
            int start = 0;
            do {
                String g = m.group(1);
                String g2 = m.group(2);
                String g3 = m.group(3);

                if (g != null) {
                    sb.append(text, start, m.start());
                    char ch1, ch2;
                    int tone;
                    switch (g.charAt(0)) {
                        case 'Í':
                            ch1 = 'I';
                            tone = 0;
                            break;
                        case 'Ì':
                            ch1 = 'I';
                            tone = 1;
                            break;
                        case 'Ỉ':
                            ch1 = 'I';
                            tone = 2;
                            break;
                        case 'Ĩ':
                            ch1 = 'I';
                            tone = 3;
                            break;
                        case 'Ị':
                            ch1 = 'I';
                            tone = 4;
                            break;
                        case 'Ó':
                            ch1 = 'O';
                            tone = 0;
                            break;
                        case 'Ò':
                            ch1 = 'O';
                            tone = 1;
                            break;
                        case 'Ỏ':
                            ch1 = 'O';
                            tone = 2;
                            break;
                        case 'Õ':
                            ch1 = 'O';
                            tone = 3;
                            break;
                        case 'Ọ':
                            ch1 = 'O';
                            tone = 4;
                            break;
                        case 'Ú':
                            ch1 = 'U';
                            tone = 0;
                            break;
                        case 'Ù':
                            ch1 = 'U';
                            tone = 1;
                            break;
                        case 'Ủ':
                            ch1 = 'U';
                            tone = 2;
                            break;
                        case 'Ũ':
                            ch1 = 'U';
                            tone = 3;
                            break;
                        case 'Ụ':
                            ch1 = 'U';
                            tone = 4;
                            break;
                        case 'í':
                            ch1 = 'i';
                            tone = 0;
                            break;
                        case 'ì':
                            ch1 = 'i';
                            tone = 1;
                            break;
                        case 'ỉ':
                            ch1 = 'i';
                            tone = 2;
                            break;
                        case 'ĩ':
                            ch1 = 'i';
                            tone = 3;
                            break;
                        case 'ị':
                            ch1 = 'i';
                            tone = 4;
                            break;
                        case 'ó':
                            ch1 = 'o';
                            tone = 0;
                            break;
                        case 'ò':
                            ch1 = 'o';
                            tone = 1;
                            break;
                        case 'ỏ':
                            ch1 = 'o';
                            tone = 2;
                            break;
                        case 'õ':
                            ch1 = 'o';
                            tone = 3;
                            break;
                        case 'ọ':
                            ch1 = 'o';
                            tone = 4;
                            break;
                        case 'ú':
                            ch1 = 'u';
                            tone = 0;
                            break;
                        case 'ù':
                            ch1 = 'u';
                            tone = 1;
                            break;
                        case 'ủ':
                            ch1 = 'u';
                            tone = 2;
                            break;
                        case 'ũ':
                            ch1 = 'u';
                            tone = 3;
                            break;
                        case 'ụ':
                        default:
                            ch1 = 'u';
                            tone = 4;
                            break;
                    }
                    switch (g.charAt(1)) {
                        case 'A':
                            ch2 = VOWEL_A[tone];
                            break;
                        case 'Ă':
                            ch2 = VOWEL_AW[tone];
                            break;
                        case 'Â':
                            ch2 = VOWEL_AA[tone];
                            break;
                        case 'E':
                            ch2 = VOWEL_E[tone];
                            break;
                        case 'Ê':
                            ch2 = VOWEL_EE[tone];
                            break;
                        case 'O':
                            ch2 = VOWEL_O[tone];
                            break;
                        case 'Ơ':
                            ch2 = VOWEL_OW[tone];
                            break;
                        case 'Ô':
                            ch2 = VOWEL_OO[tone];
                            break;
                        case 'Y':
                            ch2 = VOWEL_Y[tone];
                            break;
                        case 'a':
                            ch2 = VOWEL_a[tone];
                            break;
                        case 'ă':
                            ch2 = VOWEL_aw[tone];
                            break;
                        case 'â':
                            ch2 = VOWEL_aa[tone];
                            break;
                        case 'e':
                            ch2 = VOWEL_e[tone];
                            break;
                        case 'ê':
                            ch2 = VOWEL_ee[tone];
                            break;
                        case 'o':
                            ch2 = VOWEL_o[tone];
                            break;
                        case 'ơ':
                            ch2 = VOWEL_ow[tone];
                            break;
                        case 'ô':
                            ch2 = VOWEL_oo[tone];
                            break;
                        case 'y':
                        default:
                            ch2 = VOWEL_y[tone];
                            break;
                    }
                    sb.append(ch1).append(ch2);
                    if (g.length() == 3) {
                        sb.append(g.charAt(2));
                    }
                    start = m.end();
                } else if (g2 != null) {
                    sb.append(text, start, m.start(2));
                    char ch1 = g2.charAt(0);
                    char ch2 = g2.charAt(1);
                    int tone;
                    switch (ch2) {
                        case 'á':
                            ch2 = 'a';
                            tone = 0;
                            break;
                        case 'à':
                            ch2 = 'a';
                            tone = 1;
                            break;
                        case 'ả':
                            ch2 = 'a';
                            tone = 2;
                            break;
                        case 'ã':
                            ch2 = 'a';
                            tone = 3;
                            break;
                        case 'ạ':
                            ch2 = 'a';
                            tone = 4;
                            break;
                        case 'í':
                            ch2 = 'i';
                            tone = 0;
                            break;
                        case 'ì':
                            ch2 = 'i';
                            tone = 1;
                            break;
                        case 'ỉ':
                            ch2 = 'i';
                            tone = 2;
                            break;
                        case 'ĩ':
                            ch2 = 'i';
                            tone = 3;
                            break;
                        case 'ị':
                            ch2 = 'i';
                            tone = 4;
                            break;
                        case 'Á':
                            ch2 = 'A';
                            tone = 0;
                            break;
                        case 'À':
                            ch2 = 'A';
                            tone = 1;
                            break;
                        case 'Ả':
                            ch2 = 'A';
                            tone = 2;
                            break;
                        case 'Ã':
                            ch2 = 'A';
                            tone = 3;
                            break;
                        case 'Ạ':
                            ch2 = 'A';
                            tone = 4;
                            break;
                        case 'Í':
                            ch2 = 'I';
                            tone = 0;
                            break;
                        case 'Ì':
                            ch2 = 'I';
                            tone = 1;
                            break;
                        case 'Ỉ':
                            ch2 = 'I';
                            tone = 2;
                            break;
                        case 'Ĩ':
                            ch2 = 'I';
                            tone = 3;
                            break;
                        default:
                            ch2 = 'I';
                            tone = 4;
                            break;
                    }
                    switch (ch1) {
                        case 'u':
                            ch1 = VOWEL_u[tone];
                            break;
                        case 'ư':
                            ch1 = VOWEL_uw[tone];
                            break;
                        case 'U':
                            ch1 = VOWEL_U[tone];
                            break;
                        default:
                            ch1 = VOWEL_UW[tone];
                            break;
                    }
                    sb.append(ch1).append(ch2);
                    start = m.end(2);
                } else {
                    sb.append(text, start, m.start(3));
                    char ch1 = g3.charAt(0);
                    char ch2 = g3.charAt(1);
                    int tone;
                    switch (ch1) {
                        case 'ý':
                            ch1 = 'y';
                            tone = 0;
                            break;
                        case 'ỳ':
                            ch1 = 'y';
                            tone = 1;
                            break;
                        case 'ỷ':
                            ch1 = 'y';
                            tone = 2;
                            break;
                        case 'ỹ':
                            ch1 = 'y';
                            tone = 3;
                            break;
                        case 'ỵ':
                            ch1 = 'y';
                            tone = 4;
                            break;
                        case 'Ý':
                            ch1 = 'Y';
                            tone = 0;
                            break;
                        case 'Ỳ':
                            ch1 = 'Y';
                            tone = 1;
                            break;
                        case 'Ỷ':
                            ch1 = 'Y';
                            tone = 2;
                            break;
                        case 'Ỹ':
                            ch1 = 'Y';
                            tone = 3;
                            break;
                        default:
                            ch1 = 'Y';
                            tone = 4;
                            break;
                    }
                    if (ch2 == 'ê') {
                        ch2 = VOWEL_ee[tone];
                    } else {
                        ch2 = VOWEL_EE[tone];
                    }
                    sb.append(ch1).append(ch2);
                    start = m.end(3);
                }
            } while (m.find());
            sb.append(text, start, text.length());
            return sb.toString();
        } else {
            return text;
        }
    }

    public static void main2(String[] args) {
        char[] vietnamese = Normalizer.normalize("a á à ả ã ạ â ấ ầ ẩ ẫ ậ ă ắ ằ ẳ ẵ ặ e é è ẻ ẽ ẹ ê ế ề ể ễ ệ i í ì ỉ ĩ ị o ó ò ỏ õ ọ ô ố ồ ổ ỗ ộ ơ ớ ờ ở ỡ ợ u ú ù ủ ũ ụ ư ứ ừ ử ữ ự y ý ỳ ỷ ỹ ỵ"
                + ("a á à ả ã ạ â ấ ầ ẩ ẫ ậ ă ắ ằ ẳ ẵ ặ e é è ẻ ẽ ẹ ê ế ề ể ễ ệ i í ì ỉ ĩ ị o ó ò ỏ õ ọ ô ố ồ ổ ỗ ộ ơ ớ ờ ở ỡ ợ u ú ù ủ ũ ụ ư ứ ừ ử ữ ự y ý ỳ ỷ ỹ ỵ".toUpperCase()), Form.NFC).replaceAll("\\s+", "").toCharArray();
        Arrays.sort(vietnamese);
        StringBuilder charWithoutAccentMarks = new StringBuilder();
        StringBuilder accentMarks = new StringBuilder();

        for (int i = 0; i < vietnamese.length; i++) {
            char[] chars = Normalizer.normalize(new String(new char[]{vietnamese[i]}), Form.NFD).toCharArray();
            int markIndex = -1;
            int mark = 0;
            MARK_DETECT:
            for (int j = 0; j < chars.length; j++) {
                switch (chars[j]) {
                    case 769: //sac
                        mark = 1;
                        markIndex = j;
                        break MARK_DETECT;
                    case 768: //huyen
                        mark = 2;
                        markIndex = j;
                        break MARK_DETECT;
                    case 771: //nga
                        mark = 4;
                        markIndex = j;
                        break MARK_DETECT;
                    case 777: //hoi
                        mark = 3;
                        markIndex = j;
                        break MARK_DETECT;
                    case 803: //nang
                        mark = 5;
                        markIndex = j;
                        break MARK_DETECT;
                    default:
                }
            }
            if (markIndex > 0) {
                System.arraycopy(chars, markIndex + 1, chars, markIndex, chars.length - markIndex - 1);
                charWithoutAccentMarks.append(Normalizer.normalize(new String(chars, 0, chars.length - 1), Form.NFC));
                accentMarks.append(mark);
            } else {
                charWithoutAccentMarks.append(vietnamese[i]);
                accentMarks.append('0');
            }
        }
        System.out.println(new String(vietnamese));
        System.out.println(charWithoutAccentMarks.toString());
        System.out.println(new String(vietnamese));
        System.out.println(accentMarks.toString());


    }

    private static final Pattern SPACE = Pattern.compile("[\\x{0}- ]+");

    public static String removeControlCharacter(String string) {
        if (string == null) {
            return string;
        }
        return SPACE.matcher(string).replaceAll(" ");
    }

    private final static Pattern N = Pattern.compile("\\bn([aeiuoy" + vietnameseLowercaseCharacters() + "])");
    private final static Pattern M = Pattern.compile("\\bm");
    private final static Pattern L = Pattern.compile("\\bl([aeiuoy" + vietnameseLowercaseCharacters() + "])");
    private final static Pattern P = Pattern.compile("\\bp([aeiuoy" + vietnameseLowercaseCharacters() + "])");

    private final static Pattern D = Pattern.compile("\\bd(?:([" + vietnameseLowercaseCharacters('i') + "])|([^" + vietnameseLowercaseCharacters('i') + "]))");
    private final static Pattern GI = Pattern.compile("\\bgi");
    private final static Pattern R = Pattern.compile("\\br");
    private final static Pattern CH = Pattern.compile("\\bch");

    private final static String[] Y_CHARS = "y ý ỳ ỷ ỹ ỵ".split(" ");
    private final static String[] I_CHARS = "i í ì ỉ ĩ ị".split(" ");

    private final static Pattern SOUND_Y = Pattern.compile("(?:\\b|[ bcdghklmnrstvx]|qu)(?:" + Arrays.asList(Y_CHARS).stream().map(y -> '(' + y + ')').collect(Collectors.joining("|")) + ")\\b");
    private final static Pattern SOUND_I = Pattern.compile("(?:\\b|[ bcdghklmnrstvx]|qu)(?:" + Arrays.asList(I_CHARS).stream().map(i -> '(' + i + ')').collect(Collectors.joining("|")) + ")\\b");

    public static Map<String, Double> sound(String lowercaseText) {
        return sound(lowercaseText, new HashMap<>());
    }

    private static Double SOUND_SCORE0 = 1.;
    private static Double SOUND_SCORE1 = 0.8;
    private static Double SOUND_SCORE2 = 0.5;
    private static Double SOUND_SCORE3 = 0.3;

    public static Map<String, Double> sound(String lowercaseText, Map<String, Double> rs) {
        if (lowercaseText.indexOf('x') >= 0) {
            rs.put(lowercaseText.replace('x', 's'), SOUND_SCORE1);
        }
        if (lowercaseText.indexOf('s') >= 0) {
            rs.put(lowercaseText.replace('s', 'x'), SOUND_SCORE1);
        }
        if (lowercaseText.indexOf("tr") >= 0) {
            rs.put(lowercaseText.replace("tr", "ch"), SOUND_SCORE1);
        }
        Matcher m = CH.matcher(lowercaseText);
        if (m.find()) {
            rs.put(m.replaceAll("tr"), SOUND_SCORE1);
        }
        m = D.matcher(lowercaseText);
        if (m.find()) {
            if (m.group(1) == null) {
                rs.put(m.replaceAll("gi$2"), SOUND_SCORE1);
                rs.put(m.replaceAll("r$2"), SOUND_SCORE1);
            } else {
                rs.put(m.replaceAll("r$1"), SOUND_SCORE1);
            }
        }
        m = GI.matcher(lowercaseText);
        if (m.find()) {
            rs.put(m.replaceAll("d"), SOUND_SCORE1);
            rs.put(m.replaceAll("r"), SOUND_SCORE1);
        }
        m = R.matcher(lowercaseText);
        if (m.find()) {
            rs.put(m.replaceAll("d"), SOUND_SCORE1);
            rs.put(m.replaceAll("r"), SOUND_SCORE1);
        }
        m = P.matcher(lowercaseText);
        if (m.find()) {
            rs.put(m.replaceAll("t$1"), SOUND_SCORE1);
        }
        m = SOUND_Y.matcher(lowercaseText);
        if (m.find()) {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            do {
                for (int i = m.groupCount(); i > 0; i--) {
                    if (m.group(i) != null) {
                        sb.append(lowercaseText, start, m.start(i)).append(I_CHARS[i - 1]);
                        start = m.end(i);
                        break;
                    }
                }
            } while (m.find());
            sb.append(lowercaseText, start, lowercaseText.length());
            rs.put(sb.toString(), SOUND_SCORE0);
        }
        m = SOUND_I.matcher(lowercaseText);
        if (m.find()) {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            do {
                for (int i = m.groupCount(); i > 0; i--) {
                    if (m.group(i) != null) {
                        sb.append(lowercaseText, start, m.start(i)).append(Y_CHARS[i - 1]);
                        start = m.end(i);
                        break;
                    }
                }
            } while (m.find());
            sb.append(lowercaseText, start, lowercaseText.length());
            rs.put(sb.toString(), SOUND_SCORE0);
        }
        m = N.matcher(lowercaseText);
        if (m.find()) {
            rs.put(VietnameseUtils.removeAccent(m.replaceAll("m$1")), SOUND_SCORE3);
            rs.put(m.replaceAll("l$1"), SOUND_SCORE2);
        }
        m = L.matcher(lowercaseText);
        if (m.find()) {
            rs.put(m.replaceAll("n$1"), SOUND_SCORE2);
        }
        m = M.matcher(lowercaseText);
        if (m.find()) {
            rs.put(VietnameseUtils.removeAccent(m.replaceAll("n")), SOUND_SCORE3);
        }
        return rs;
    }

    public static void main(String[] args) {
//        main2(args);
        //System.out.println(removeAccentMark('ệ'));
        System.out.println(Utils.convertNoUnicodeNormal(VietnameseUtils.normalize("Hà Nôị")));
        System.out.println(Utils.convertNoUnicodeNormal(VietnameseUtils.normalize("Hà Nội")));
        System.out.println(Utils.convertNoUnicodeNormal(VietnameseUtils.normalize("Nam Định")));

    }
}
