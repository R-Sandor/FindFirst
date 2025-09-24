import axios from "axios";
import { parseData } from "./Api";
const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/user";

export const userApiInstance = axios.create({
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
    return userApiInstance({
      method: method,
      url: resource,
      data,
      ...config,
    });
  },
  userInfo() {
    return userApiInstance.get("/user-info");
  },
  oauth2Providers() {
    return userApiInstance.get("/oauth2Providers");
  },
};

export default userApi;
