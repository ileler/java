import { NgModule, Optional, SkipSelf, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Http } from '@angular/http';

import { HttpService } from './http';
import { AppState } from './app-state.service';

export class HttpUtilConfig {
  constructor(
    public context = ''
  ) {}
}

export function getHttpService(context: string) {
  return (http: Http) => new HttpService({ context: context }, http);
}

@NgModule({
	imports: [ CommonModule ]
})
export class CoreModule {

	constructor (@Optional() @SkipSelf() parentModule: CoreModule) {
		if (parentModule) {
			throw new Error('CoreModule is already loaded. Import it in the AppModule only');
		}
	}

	static forRoot(config: HttpUtilConfig): ModuleWithProviders {
    return {
      ngModule: CoreModule,
      providers: [
        AppState,
        { provide: HttpService, useFactory: getHttpService(config.context), deps: [ Http ] }
      ]
    };
  }

}
