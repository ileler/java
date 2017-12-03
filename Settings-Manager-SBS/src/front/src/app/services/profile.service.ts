/**
 * Created by zhangle on 2017/3/20.
 */
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';

import { PromiseExt, RespObjModel, HttpService } from '../core';
import { AppState } from "../core";

@Injectable()
export class ProfileService {

  private env = '/profile';

  constructor(
    private httpService: HttpService
  ) {}

  public get(env: string, sid?: string): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env, null, { env: env, sid: sid });
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
