import {MessageInstance} from 'ant-design-vue/es/message/interface';
import CryptoJS from 'crypto-js';
import JSEncrypt from 'jsencrypt';
import JsRsaSign from 'jsrsasign';
import MD5 from 'js-md5';

export class Operate {
    public static messageApi: MessageInstance | null = null;

    public static error(msg: string): void {
        if (Operate.messageApi) Operate.messageApi.error(msg);
    }

    public static success(msg: string = '操作成功'): void {
        if (Operate.messageApi) Operate.messageApi.success(msg);
    }

    public static info(msg: string): void {
        if (Operate.messageApi) Operate.messageApi.info(msg);
    }

    public static warning(msg: string): void {
        if (Operate.messageApi) Operate.messageApi.warning(msg);
    }
}

export class AESUtil {
    public static encrypt(data: string, key: string): string {
        let srcs = CryptoJS.enc.Utf8.parse(data);
        let encrypted = CryptoJS.AES.encrypt(srcs, CryptoJS.MD5(key), {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7,
        });
        return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
    };

    public static decrypt(data: string, key: string): string {
        let base64 = CryptoJS.enc.Base64.parse(data);
        let params: any = {ciphertext: base64};
        let decrypt = CryptoJS.AES.decrypt(params, CryptoJS.MD5(key), {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        let decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
        return decryptedStr.toString();
    }
}

export class RSAUtil {
    public static generateKey() {
        const keyPair = JsRsaSign.KEYUTIL.generateKeypair("RSA", 1024);
        const publicKey = JsRsaSign.KEYUTIL.getPEM(keyPair.pubKeyObj);
        const privateKey = JsRsaSign.KEYUTIL.getPEM(keyPair.prvKeyObj, "PKCS8PRV");
        return {public: publicKey, private: privateKey};
    }

    public static encrypt(data: string, key: string) {
        const encryptor = new JSEncrypt();
        encryptor.setPublicKey(key);
        return encryptor.encrypt(data);
    }

    public static decrypt(data: string, key: string) {
        const encryptor = new JSEncrypt();
        encryptor.setPrivateKey(key);
        return encryptor.decrypt(data);
    }
}

export class MD5Util {
    public static encrypt(data: string) {
        return MD5.md5(data);
    }
}

export function dataCopy(input: any): any {
    if (input instanceof Array) return copyArray(input);
    let out: any = {};
    for (let name of Object.getOwnPropertyNames(input)) {
        let data = input[name];
        if (data instanceof Array) {
            out[name] = copyArray(data);
        } else if (data instanceof Object) {
            out[name] = dataCopy(data);
        }
        else out[name] = data;
    }
    return out;
}

export function copyArray(input: object): any {
    let out: any[] = []
    if (input instanceof Array) {
        for (let i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
    }
    return out
}
