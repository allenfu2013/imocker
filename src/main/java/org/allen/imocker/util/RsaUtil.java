package org.allen.imocker.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.allen.imocker.util.RsaConst.KEY_ALGORITHM;
import static org.allen.imocker.util.RsaConst.KEY_X509;

public class RsaUtil {

    public static PrivateKey loadPrivateKey(String path) {
        try {
            final byte[] bytes = IOUtils.toByteArray(RsaUtil.class.getClassLoader().getResourceAsStream(path));
            final KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            return factory.generatePrivate(spec);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey loadPublicKey(String path) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(RsaUtil.class.getClassLoader().getResourceAsStream(path)));
            String line;
            final StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("-")) {
                    builder.append(line);
                }
            }
            final CertificateFactory factory = CertificateFactory.getInstance(KEY_X509);
            final Certificate certificate = factory.generateCertificate(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(builder.toString())));
            return certificate.getPublicKey();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void main(String[] argv) throws Exception {
        /*
         * openssl req -x509 -newkey rsa:1024 -keyout private_key.pem -out baofoo_imocker.cer -days 3650
         * openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -nocrypt > private_key_pkcs8.pem
         */
        final PrivateKey privateKey = RsaUtil.loadPrivateKey("payment/baofoo/private_key_pkcs8.pem");
        final PublicKey publicKey = RsaUtil.loadPublicKey("payment/baofoo/baofoo_imocker.cer");
        System.err.println(privateKey);
        System.err.println(publicKey);

        final JSONObject jsonObject = JSONObject.parseObject("{\"a\":\"b\",\"c\":{\"d\":\"e\"}}");
        String json = jsonObject.toJSONString();

        json = SecurityUtil.Base64Encode(json);
        json = RsaCodingUtil.encryptByPrivateKey(json, privateKey);
        System.err.println(json);

        json = RsaCodingUtil.decryptByPublicKey(json, publicKey);
        json = SecurityUtil.Base64Decode(json);
        System.err.println(json);
    }

}
