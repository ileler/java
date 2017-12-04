import { Component, ViewChild } from '@angular/core';
import { Env } from 'app/domain/env';
import { Profile } from 'app/domain/profile';
import { Server } from 'app/domain/server';
import { SecService } from 'app/services/sec.service';
import { EnvService } from 'app/services/env.service';
import { ProfileService } from 'app/services/profile.service';
import { ServerService } from 'app/services/server.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SecService, EnvService, ProfileService, ServerService]
})
export class AppComponent {

    isLogin: boolean;

    username: string;

    login: any = {};

    newUser: any = {};

    displayLoginDialog: boolean;

    displayAddUserDialog: boolean;

    @ViewChild("pdt") pdt: any;

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

    constructor(private secService: SecService, private envService: EnvService, private profileService: ProfileService, private serverService: ServerService) { }

    ngOnInit() {
        this.load();
        this.secService.isLogin().then(
          ((respObj) => {
            if (respObj.success()) {
              //login success
              this.isLogin = true;
              this.username = respObj.data;
            } else {
              console.log(respObj);
            }
          }).bind(this),
          (error) => {
            console.log(error);
          }
        );
    }

    showDialogToAddUser() {
        this.displayAddUserDialog = true;
        this.newUser = {};
    }

    addUser() {
        this.secService.add(this.newUser).then(
          ((respObj) => {
            alert('add success');
          }).bind(this),
          (error) => {
            alert('add failed.');
          }
        );
        this.displayAddUserDialog = false;
    }

    showDialogToLogin() {
        this.displayLoginDialog = true;
        this.login = {};
    }

    modify() {
      this.secService.modify(this.login).then(
        ((respObj) => {
          if (respObj.success()) {
            //login success
            this.isLogin = true;
            this.username = respObj.data;
            this.displayLoginDialog = false;
          } else {
            alert('modify failed.');
          }
        }).bind(this),
        (error) => {
          alert('modify failed.');
        }
      );
    }

    toLogin() {
      this.secService.login(this.login).then(
        ((respObj) => {
          if (respObj.success()) {
            //login success
            this.isLogin = true;
            this.username = respObj.data;
            this.displayLoginDialog = false;
            this.load();
          } else {
            alert('login failed, username or password invalid.');
          }
        }).bind(this),
        (error) => {
          alert('login failed, username or password invalid.');
        }
      );
    }

    toLogout() {
      this.isLogin = false;
      this.username = null;
      this.secService.logout().then(
        ((respObj) => {
          this.load();
        }).bind(this),
        (error) => {
        }
      );
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
      this.allServer = (!this.envs || this.envs.length < 1) ? [] : [{label:'ALL', value:{id: null}}];
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
            for(let i = 0, j = envs.length; i < j; i++) {
              if (envs[i].name == this.env.name) {
                alert('Env already exists.');
                return false;
              }
            }
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
        _profile.sid = this.profile.sid && this.profile.sid.id || this.servers[0].id;
        if(this.newProfile) {
            for(let i = 0, j = profiles.length; i < j; i++) {
              if (profiles[i].id == _profile.id) {
                alert('Profile already exists.');
                return false;
              }
            }
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
        this.servers.forEach((server) => {
          if (server.id == this.profile.sid) this.profile.sid = server;
        });
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
        delete this.server['url'];
        if(this.newServer) {
            for(let i = 0, j = servers.length; i < j; i++) {
              if (servers[i].id == this.server.id) {
                alert('Server already exists.');
                return false;
              }
            }
            this.serverService.add(this.currentEnv.name, this.server).then(
              ((respObj) => {
                console.log(respObj);
                if (respObj.success()) {
                  servers.push(respObj.getData());
                  this.allServer.push({label:respObj.getData().id, value:respObj.getData()});
                  this.servers = servers;
                }
              }).bind(this),
              (error) => {
                console.log(error);
              }
            );
        } else {
            this.serverService.mod(this.currentEnv.name, this.server).then(
              ((respObj) => {
                  console.log(respObj);
                  if (respObj.success()) {
                    servers[this.findSelectedServerIndex()] = respObj.getData();
                    this.servers = servers;
                  }
              }).bind(this),
              (error) => {
                console.log(error);
              }
            );
        }
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
        this.allServer = this.allServer.filter((val,i) => val.label != this.selectedServer.id);
        if (this.currentServer.id == this.selectedServer.id) this.currentServer = this.allServer[0].value;
        this.serverChange();
        this.server = null;
        this.displayServerDialog = false;
    }

    serverChange() {
      this.pdt.filter(this.currentServer&&this.currentServer.id||null,'sid','equals');
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
  constructor(public id?, public host?, public port?, public shome?, public username?, public password?, public configuration?) {}
}
