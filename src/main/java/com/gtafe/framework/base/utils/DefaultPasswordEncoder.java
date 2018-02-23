package com.gtafe.framework.base.utils;
import org.springframework.util.StringUtils;

//import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class DefaultPasswordEncoder{

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                                                '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    //@NotNull
    private final String encodingAlgorithm;

    private String characterEncoding;

    public DefaultPasswordEncoder(final String encodingAlgorithm) {
        this.encodingAlgorithm = encodingAlgorithm;
    }

    public String encode(final String password) {
        if (password == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest
                .getInstance(this.encodingAlgorithm);

            if (StringUtils.hasText(this.characterEncoding)) {
                messageDigest.update(password.getBytes(this.characterEncoding));
            } else {
                messageDigest.update(password.getBytes());
            }


            final byte[] digest = messageDigest.digest();

            return getFormattedText(digest);
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private String getFormattedText(final byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public void setCharacterEncoding(final String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    public static void main(String[] args) {
    	DefaultPasswordEncoder encoder=new DefaultPasswordEncoder("MD5");
    	encoder.setCharacterEncoding("UTF-8");
    	System.out.println(encoder.encode("gta@2014"));
    	//System.out.println("OKKKKKKKK");
    	//9414f9301cdb492b4dcd83f8c711d8bb4
	}
}
