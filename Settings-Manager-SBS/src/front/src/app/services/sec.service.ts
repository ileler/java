/**
 * Created by zhangle on 2017/3/20.
 */
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';

import { PromiseExt, RespObjModel, HttpService } from '../core';
import { AppState } from "../core";

@Injectable()
export class SecService {

  private sec = '/sec';

  constructor(
    private httpService: HttpService
  ) {}

  public isLogin(): PromiseExt<RespObjModel> {
    return this.httpService.get(this.sec + "/login", null, null);
  }

  public add(data: any): PromiseExt<RespObjModel> {
    return this.httpService.post(this.sec + "/add", null, null, data);
  }

  public login(data: any): PromiseExt<RespObjModel> {
    return this.httpService.post(this.sec + "/login", null, null, data);
  }

  public modify(data: any): PromiseExt<RespObjModel> {
    return this.httpService.put(this.sec + "/modify", null, null, data);
  }

  public logout(): PromiseExt<RespObjModel> {
    return this.httpService.delete(this.sec + "/login", null, null);
  }

}
