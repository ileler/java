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

    services: any = {};

    serviceSID: string;

    displayServicesDialog: boolean;

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
            //login success
            this.isLogin = true;
            this.username = respObj.data;
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
          //login success
          this.isLogin = true;
          this.username = respObj.data;
          this.displayLoginDialog = false;
        }).bind(this),
        (error) => {
          alert('modify failed.');
        }
      );
    }

    toLogin() {
      this.secService.login(this.login).then(
        ((respObj) => {
          //login success
          this.isLogin = true;
          this.username = respObj.data;
          this.displayLoginDialog = false;
          this.load();
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

    showServerMsg(server) {
      server.connectMessage && alert(server.connectMessage);
    }

    showServiceDialog(sid) {
      this.displayServicesDialog = true;
      this.serviceSID = sid
    }

    killService(pid) {
      this.serverService.kill(this.currentEnv.name, this.serviceSID, pid).then(
        (respObj) => {
          this.servers.forEach((server) => {
            if (server.id === this.serviceSID) {
              this.loadServices(this.currentEnv.name, server);
            }
          });
        },
        (error) => {
          console.log(error);
        }
      );
    }

    loadServices(env, server) {
      this.serverService.list(env, server.id).then(
        (respObj) => {
          let data = respObj.getData({});
          if (0 !== data.exitCode) {
            server.connectMessage = data.err;
          } else {
            let skey = env + '-' + server.id;
            this.services[skey] = [];
            (data.out || '').split('\n').forEach((service) => {
              if (!service || !service.trim() || service.indexOf('sun.tools.jps.Jps') != -1) return;
              this.services[skey].push({pid:service.substring(0, service.indexOf(' ')), detail:service.substring(service.indexOf(' ') + 1)});
            });
            server.connectMessage = null;
          }
        },
        (error) => {
          server.connectMessage = error.mesg || 'failed';
        }
      );
    }

    checkServiceNo(sid) {
      return ((this.profiles || []).filter((val,i) => val.sid == sid)).length === (this.services[this.currentEnv.name + '-' + sid] || []).length;
    }

    showServerLoginLogs(server) {
      this.serverService.loginLogs(this.currentEnv.name, server.id).then(
        (respObj) => {
          alert(respObj.getData());
        },
        (error) => {
          console.log(error);
        }
      );
    }

    showServerOperLogs(server) {
      this.serverService.operLogs(this.currentEnv.name, server.id).then(
        (respObj) => {
          alert(respObj.getData());
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
          ((respObj) => {
            this.servers = respObj.getData([]);
            this.servers.forEach((server) => {
              this.allServer.push({label:server.id, value:server});
              this.loadServices(this.currentEnv.name, server);
            });
            this.currentServer = this.allServer.length > 0 ? this.allServer[0].value : null;
          }).bind(this),
          (error) => {
            console.log(error);
          }
        );
        this.profileService.get(this.currentEnv.name).then(
          (respObj) => {
            this.profiles = respObj.getData([]);
          },
          (error) => {
            console.log(error);
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
      //     (error) => {
      //       console.log(error);
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
        if (!this.env.name || !this.env.path) {
          alert('args invalid.');
          return;
        }
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
        if (!this.profile.id || !this.profile.sid || !this.profile.port) {
          alert('args invalid.');
          return;
        }
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
        if (!this.server.id || !this.server.host || !this.server.username || !this.server.password) {
          alert('args invalid.');
          return;
        }
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
                let server = respObj.getData();
                servers.push(server);
                this.allServer.push({label:server.id, value:server});
                this.loadServices(this.currentEnv.name, server);
                this.servers = servers;
              }).bind(this),
              (error) => {
                console.log(error);
              }
            );
        } else {
            this.serverService.mod(this.currentEnv.name, this.server).then(
              ((respObj) => {
                console.log(respObj);
                let server = respObj.getData();
                servers[this.findSelectedServerIndex()] = server;
                this.loadServices(this.currentEnv.name, server);
                this.servers = servers;
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
  constructor(public id?, public host?, public port?, public shome?, public username?, public password?, public configuration?, public connectMessage?) {}
}
