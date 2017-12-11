/**
 * Created by zhangle on 2017/3/20.
 */
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';

import { PromiseExt, RespObjModel, HttpService } from '../core';
import { AppState } from "../core";

@Injectable()
export class ServerService {

  private env = '/server';

  constructor(
    private httpService: HttpService
  ) {}

  public list(env: string, id: string): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env + "/services", null, { env: env, id: id });
  }

  public kill(env: string, id: string, pid: string): PromiseExt<RespObjModel> {
    return this.httpService.delete(this.env + "/kill", null, { env: env, id: id, pid: pid });
  }

  public operLogs(env: string, id: string): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env + "/oper-logs", null, { env: env, id: id });
  }

  public loginLogs(env: string, id: string): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env + "/login-logs", null, { env: env, id: id });
  }

  public get(env: string): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env, null, { env: env });
  }

  public add(env: string, data: any): PromiseExt<RespObjModel> {
    return this.httpService.post(this.env, null, { env: env }, data);
  }

  public mod(env: string, data: any): PromiseExt<RespObjModel> {
    return this.httpService.put(this.env, null, { env: env }, data);
  }

  public del(env: string, id: string): PromiseExt<RespObjModel> {
    return this.httpService.delete(this.env, null, { env: env, id: id });
  }

}
