/**
 * Created by zhangle on 2017/3/20.
 */
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';

import { PromiseExt, RespObjModel, HttpService } from '../core';
import { AppState } from "../core";

@Injectable()
export class EnvService {

  private env = '/env';

  constructor(
    private appState: AppState,
    private httpService: HttpService
  ) {}

  public get(): PromiseExt<RespObjModel> {
    return this.httpService.get(this.env, null, null);
  }

  public add(data: any): PromiseExt<RespObjModel> {
    return this.httpService.post(this.env, null, null, data);
  }

  public mod(data: any): PromiseExt<RespObjModel> {
    return this.httpService.put(this.env, null, null, data);
  }

  public del(name: string): PromiseExt<RespObjModel> {
    return this.httpService.delete(this.env, null, { name: name });
  }

}
