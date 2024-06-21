import axios from "axios";
import {Operate} from "../util";
import {goToLogin} from "../router";
import Config from "../config";

export class Request {
    public static baseURL: string = `${Config.url}/api`;
    public static instance = axios.create({
        baseURL: Request.baseURL,
        timeout: 15000
    })

    public static async post(link: string, data: object, callback: (response: any) => void, verify: boolean = true) {
        await Request.instance.post(link, data, this.defaultHandler())
            .then(res => Request.call(res.data, callback, verify))
            .catch(reason => Request.axiosError(reason))
    }

    public static async get(link: string, callback: (response: any) => void, verify: boolean = true) {
        await Request.instance.get(link, this.defaultHandler())
            .then(res => Request.call(res.data, callback, verify))
            .catch(reason => Request.axiosError(reason))
    }

    public static async put(link: string, data: object, callback: (response: any) => void, verify: boolean = true) {
        await Request.instance.put(link, data, this.defaultHandler())
            .then(res => Request.call(res.data, callback, verify))
            .catch(reason => Request.axiosError(reason))
    }

    public static async del(link: string, callback: (response: any) => void, verify: boolean = true) {
        await Request.instance.delete(link, this.defaultHandler())
            .then(res => Request.call(res.data, callback, verify))
            .catch(reason => Request.axiosError(reason))
    }

    private static async call(context: any, callback: (response: any) => void, verify: boolean) {
        if (verify) {
            if (context.code == 200) {
                callback(context);
            } else if (context.code == -2 || context.code == 401) {
                goToLogin();
            } else {
                Operate.error(context.msg);
            }
        } else {
            callback(context);
        }
    }

    private static axiosError(reason: any) {
        Operate.error(reason.message);
    }

    public static defaultHandler(): { headers: { AuthToken: string } } {
        return {
            headers: {
                AuthToken: `${localStorage.getItem("token")}`
            }
        }
    }
}
