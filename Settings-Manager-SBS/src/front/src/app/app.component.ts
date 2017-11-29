import { Component } from '@angular/core';
import { Env } from 'app/domain/env';
import { Profile } from 'app/domain/profile';
import { Server } from 'app/domain/server';
import { EnvService } from 'app/services/env.service';
import { ProfileService } from 'app/services/profile.service';
import { ServerService } from 'app/services/server.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [EnvService, ProfileService, ServerService]
})
export class AppComponent {

    allEnv: any[] = [];

    currentEnv: Env = new PrimeEnv();

    allServer: any[] = [];

    currentServer: Server = new PrimeServer();

    displayEnvDialog: boolean;

    displayProfileDialog: boolean;

    displayServerDialog: boolean;

    env: Env = new PrimeEnv();

    selectedEnv: Env;

    newEnv: boolean;

    envs: Env[];

    profile: Profile = new PrimeProfile();

    selectedProfile: Profile;

    newProfile: boolean;

    profiles: Profile[];

    server: Server = new PrimeServer();

    selectedServer: Server;

    newServer: boolean;

    servers: Server[];

    constructor(private envService: EnvService, private profileService: ProfileService, private serverService: ServerService) { }

    ngOnInit() {
        this.load();
    }

    load() {
        this.allEnv = [];
        this.envService.get().then(
          (respObj) => {
            this.envs = respObj.getData([]);
            this.envs.forEach((env) => {
              this.allEnv.push({label:env.name, value:env});
            });
            this.currentEnv = this.envs.length > 0 ? this.envs[0] : null;
            this.changeCurrentEnv();
          },
          (error) => {
            console.log(error);
          }
        );
    }

    changeCurrentEnv() {
      this.allServer = [{label:'ALL', value:{id: null}}];
      if (this.currentEnv) {
        this.serverService.get(this.currentEnv.name).then(
          (respObj) => {
            this.servers = respObj.getData([]);
            this.servers.forEach((server) => {
              this.allServer.push({label:server.id, value:server});
            });
            this.currentServer = this.allServer.length > 0 ? this.allServer[0].value : null;
          },
          (eroor) => {
            console.log(eroor);
          }
        );
        this.profileService.get(this.currentEnv.name).then(
          (respObj) => {
            this.profiles = respObj.getData([]);
          },
          (eroor) => {
            console.log(eroor);
          }
        );
      }
    }

    changeCurrentServer() {
      // if (this.currentEnv && this.currentServer) {
      //   this.profileService.get(this.currentEnv.name, this.currentServer.id).then(
      //     (respObj) => {
      //       this.profiles = respObj.getData([]);
      //     },
      //     (eroor) => {
      //       console.log(eroor);
      //     }
      //   );
      // }
    }

    showDialogToAddEnv() {
        this.newEnv = true;
        this.env = new PrimeEnv();
        this.displayEnvDialog = true;
    }

    saveEnv() {
        let envs = [...this.envs];
        if(this.newEnv) {
            envs.push(this.env);
            this.envService.add(this.env).then(
              (respObj) => {
                console.log(respObj);
                this.load();
              },
              (error) => {
                console.log(error);
              }
            );
        } else {
            envs[this.findSelectedEnvIndex()] = this.env;
            this.envService.mod(this.env).then(
              (respObj) => {
                console.log(respObj);
                this.load();
              },
              (error) => {
                console.log(error);
              }
            );
        }
        this.envs = envs;
        this.env = null;
        this.displayEnvDialog = false;
    }

    deleteEnv() {
        this.envService.del(this.selectedEnv.name).then(
          (respObj) => {
            console.log(respObj);
            this.load();
          },
          (error) => {
            console.log(error);
          }
        );
        let index = this.findSelectedEnvIndex();
        this.envs = this.envs.filter((val,i) => i!=index);
        this.env = null;
        this.displayEnvDialog = false;
    }

    onRowSelectEnv(event) {
        this.newEnv = false;
        this.env = this.cloneEnv(event.data);
        this.displayEnvDialog = true;
    }

    cloneEnv(c: Env): Env {
        let env = new PrimeEnv();
        for(let prop in c) {
            env[prop] = c[prop];
        }
        return env;
    }

    findSelectedEnvIndex(): number {
        return this.envs.indexOf(this.selectedEnv);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    showDialogToAddProfile() {
        if (this.currentEnv == null)  return;
        this.newProfile = true;
        this.profile = new PrimeProfile();
        this.profile.sid = this.currentServer.id && this.currentServer || (this.servers && this.servers.length > 0 ? this.servers[0] : null);
        this.displayProfileDialog = true;
    }

    saveProfile() {
        let profiles = [...this.profiles];
        let _profile = this.cloneProfile(this.profile);
        _profile.sid = this.profile.sid.id;
        if(this.newProfile) {
            profiles.push(_profile);
            this.profileService.add(this.currentEnv.name, _profile).then(
              (respObj) => {
                console.log(respObj);
              },
              (error) => {
                console.log(error);
              }
            );
        } else {
            profiles[this.findSelectedProfileIndex()] = _profile;
            this.profileService.mod(this.currentEnv.name, _profile).then(
              (respObj) => {
                console.log(respObj);
              },
              (error) => {
                console.log(error);
              }
            );
        }
        this.profiles = profiles;
        this.profile = null;
        this.displayProfileDialog = false;
    }

    deleteProfile() {
        this.profileService.del(this.currentEnv.name, this.selectedProfile.id).then(
          (respObj) => {
            console.log(respObj);
          },
          (error) => {
            console.log(error);
          }
        );
        let index = this.findSelectedProfileIndex();
        this.profiles = this.profiles.filter((val,i) => i!=index);
        this.profile = null;
        this.displayProfileDialog = false;
    }

    onRowSelectProfile(event) {
        this.newProfile = false;
        this.profile = this.cloneProfile(event.data);
        this.displayProfileDialog = true;
    }

    cloneProfile(c: Profile): Profile {
        let profile = new PrimeProfile();
        for(let prop in c) {
          profile[prop] = c[prop];
        }
        return profile;
    }

    findSelectedProfileIndex(): number {
        return this.profiles.indexOf(this.selectedProfile);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    showDialogToAddServer() {
        if (this.currentEnv == null)  return;
        this.newServer = true;
        this.server = new PrimeServer();
        this.displayServerDialog = true;
    }

    saveServer() {
        let servers = [...this.servers];
        if(this.newServer) {
            servers.push(this.server);
            this.serverService.add(this.currentEnv.name, this.server).then(
              (respObj) => {
                console.log(respObj);
              },
              (error) => {
                console.log(error);
              }
            );
        } else {
            servers[this.findSelectedServerIndex()] = this.server;
            this.serverService.mod(this.currentEnv.name, this.server).then(
              (respObj) => {
                console.log(respObj);
              },
              (error) => {
                console.log(error);
              }
            );
        }
        this.servers = servers;
        this.server = null;
        this.displayServerDialog = false;
    }

    deleteServer() {
        this.serverService.del(this.currentEnv.name, this.selectedServer.id).then(
          (respObj) => {
            console.log(respObj);
          },
          (error) => {
            console.log(error);
          }
        );
        let index = this.findSelectedServerIndex();
        this.servers = this.servers.filter((val,i) => i!=index);
        this.server = null;
        this.displayServerDialog = false;
    }

    onRowSelectServer(event) {
        this.newServer = false;
        this.server = this.cloneServer(event.data);
        this.displayServerDialog = true;
    }

    cloneServer(c: Server): Server {
        let server = new PrimeServer();
        for(let prop in c) {
          server[prop] = c[prop];
        }
        return server;
    }

    findSelectedServerIndex(): number {
        return this.servers.indexOf(this.selectedServer);
    }
}

export class PrimeEnv implements Env {
    constructor(public name?, public path?, public template?) {}
}

export class PrimeProfile implements Profile {
  constructor(public id?, public sid?, public dir?, public port?, public dPort?, public arg?) {}
}

export class PrimeServer implements Server {
  constructor(public id?, public url?, public username?, public password?, public configuration?) {}
}
