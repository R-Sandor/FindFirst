import axios from "axios";
import { parseData } from "./Api";
const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/user";

export const instance = axios.create({
  withCredentials: true,
  baseURL: SERVER_URL,
  timeout: 10000,
  transformResponse: [
    function (data: any) {
      return parseData(data);
    },
  ],
});

const userApi = {
  async execute(
    method: string,
    resource: string,
    data: any,
    config: {} | undefined,
  ) {
    return instance({
      method: method,
      url: resource,
      data,
      ...config,
    });
  },
  userInfo() { 
    return instance.get("/user-info")
  }
}

export default userApi;
