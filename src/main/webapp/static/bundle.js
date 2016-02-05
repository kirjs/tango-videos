webpackJsonp([0],{

/***/ 0:
/***/ function(module, exports, __webpack_require__) {

	var browser_1 = __webpack_require__(1);
	var http_1 = __webpack_require__(223);
	var router_1 = __webpack_require__(238);
	var ProfileService_1 = __webpack_require__(262);
	var VideoService_1 = __webpack_require__(267);
	var tango_videos_1 = __webpack_require__(268);
	var DancerService_1 = __webpack_require__(283);
	browser_1.bootstrap(tango_videos_1.TangoVideos, [
	    http_1.HTTP_PROVIDERS,
	    router_1.ROUTER_PROVIDERS,
	    ProfileService_1.CurrentUserService,
	    VideoService_1.VideoService,
	    DancerService_1.DancerService
	])
	    .catch(function (err) { return console.error(err); });


/***/ },

/***/ 262:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var http_1 = __webpack_require__(223);
	__webpack_require__(263);
	var CurrentUserService = (function () {
	    function CurrentUserService(http) {
	        this.http = http;
	    }
	    CurrentUserService.prototype.getCurrentUser = function () {
	        return this.makeRequest();
	    };
	    CurrentUserService.prototype.makeRequest = function () {
	        var url = "/api/currentUser";
	        return this.http.get(url).map(function (res) { return res.json(); });
	    };
	    CurrentUserService.prototype.login = function (credentials) {
	        var headers = new http_1.Headers();
	        headers.append('Content-Type', 'application/x-www-form-urlencoded');
	        var creds = "username=" + credentials.username + "&password=" + credentials.password;
	        return this.http.post('api/login/', creds, { headers: headers }).map(function (res) { return res.json(); });
	    };
	    CurrentUserService.prototype.logout = function () {
	        return this.http.get('api/logout');
	    };
	    CurrentUserService = __decorate([
	        core_1.Injectable(), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], CurrentUserService);
	    return CurrentUserService;
	})();
	exports.CurrentUserService = CurrentUserService;


/***/ },

/***/ 263:
/***/ function(module, exports, __webpack_require__) {

	var Observable_1 = __webpack_require__(65);
	var map_1 = __webpack_require__(264);
	Observable_1.Observable.prototype.map = map_1.map;
	//# sourceMappingURL=map.js.map

/***/ },

/***/ 264:
/***/ function(module, exports, __webpack_require__) {

	var __extends = (this && this.__extends) || function (d, b) {
	    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
	    function __() { this.constructor = d; }
	    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
	};
	var Subscriber_1 = __webpack_require__(66);
	var tryCatch_1 = __webpack_require__(265);
	var errorObject_1 = __webpack_require__(266);
	/**
	 * Similar to the well known `Array.prototype.map` function, this operator
	 * applies a projection to each value and emits that projection in the returned observable
	 *
	 * @param {Function} project the function to create projection
	 * @param {any} [thisArg] an optional argument to define what `this` is in the project function
	 * @returns {Observable} a observable of projected values
	 */
	function map(project, thisArg) {
	    if (typeof project !== 'function') {
	        throw new TypeError('argument is not a function. Are you looking for `mapTo()`?');
	    }
	    return this.lift(new MapOperator(project, thisArg));
	}
	exports.map = map;
	var MapOperator = (function () {
	    function MapOperator(project, thisArg) {
	        this.project = project;
	        this.thisArg = thisArg;
	    }
	    MapOperator.prototype.call = function (subscriber) {
	        return new MapSubscriber(subscriber, this.project, this.thisArg);
	    };
	    return MapOperator;
	})();
	var MapSubscriber = (function (_super) {
	    __extends(MapSubscriber, _super);
	    function MapSubscriber(destination, project, thisArg) {
	        _super.call(this, destination);
	        this.project = project;
	        this.thisArg = thisArg;
	        this.count = 0;
	    }
	    MapSubscriber.prototype._next = function (x) {
	        var result = tryCatch_1.tryCatch(this.project).call(this.thisArg || this, x, this.count++);
	        if (result === errorObject_1.errorObject) {
	            this.error(errorObject_1.errorObject.e);
	        }
	        else {
	            this.destination.next(result);
	        }
	    };
	    return MapSubscriber;
	})(Subscriber_1.Subscriber);
	//# sourceMappingURL=map.js.map

/***/ },

/***/ 265:
/***/ function(module, exports, __webpack_require__) {

	var errorObject_1 = __webpack_require__(266);
	var tryCatchTarget;
	function tryCatcher() {
	    try {
	        return tryCatchTarget.apply(this, arguments);
	    }
	    catch (e) {
	        errorObject_1.errorObject.e = e;
	        return errorObject_1.errorObject;
	    }
	}
	function tryCatch(fn) {
	    tryCatchTarget = fn;
	    return tryCatcher;
	}
	exports.tryCatch = tryCatch;
	;
	//# sourceMappingURL=tryCatch.js.map

/***/ },

/***/ 266:
/***/ function(module, exports) {

	exports.errorObject = { e: {} };
	//# sourceMappingURL=errorObject.js.map

/***/ },

/***/ 267:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var http_1 = __webpack_require__(223);
	__webpack_require__(263);
	var VideoService = (function () {
	    function VideoService(http) {
	        this.http = http;
	    }
	    VideoService.prototype.makeRequest = function (url) {
	        url = '/api/videos' + url;
	        return this.http.get(url).map(function (res) { return res.json(); });
	    };
	    VideoService.prototype.list = function () {
	        return this.makeRequest("/list");
	    };
	    VideoService.prototype.addDancer = function (id, dancer) {
	        var headers = new http_1.Headers();
	        headers.append('Content-Type', 'application/x-www-form-urlencoded');
	        return this.http.post("api/videos/" + id + "/dancers/add", 'name=' + dancer, { headers: headers }).map(function (res) { return res.json(); });
	    };
	    VideoService.prototype.removeDancer = function (id, dancer) {
	        var headers = new http_1.Headers();
	        headers.append('Content-Type', 'application/x-www-form-urlencoded');
	        return this.http.post("api/videos/" + id + "/dancers/remove", 'name=' + dancer, { headers: headers }).map(function (res) { return res.json(); });
	    };
	    VideoService = __decorate([
	        core_1.Injectable(), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], VideoService);
	    return VideoService;
	})();
	exports.VideoService = VideoService;


/***/ },

/***/ 268:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var router_1 = __webpack_require__(238);
	var home_1 = __webpack_require__(269);
	var videoForm_1 = __webpack_require__(272);
	var userProfile_1 = __webpack_require__(279);
	var dancer_1 = __webpack_require__(282);
	var explore_1 = __webpack_require__(295);
	var TangoVideos = (function () {
	    function TangoVideos() {
	    }
	    TangoVideos = __decorate([
	        core_1.Component({
	            selector: 'tango-videos',
	            providers: [],
	            template: __webpack_require__(298),
	            directives: [router_1.ROUTER_DIRECTIVES.concat(userProfile_1.UserProfile)],
	            pipes: []
	        }),
	        router_1.RouteConfig([
	            new router_1.Route({ path: '/videos', component: explore_1.Explore, name: 'Videos', useAsDefault: true }),
	            new router_1.Route({ path: '/dancers/:id', component: dancer_1.Dancer, name: 'Dancers' }),
	            new router_1.Route({ path: '/home', component: home_1.Home, name: 'Home' }),
	            new router_1.Route({ path: '/new/video', component: videoForm_1.VideoForm, name: 'AddVideo' }),
	        ]), 
	        __metadata('design:paramtypes', [])
	    ], TangoVideos);
	    return TangoVideos;
	})();
	exports.TangoVideos = TangoVideos;


/***/ },

/***/ 269:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var Home = (function () {
	    function Home() {
	    }
	    Home = __decorate([
	        core_1.Component({
	            selector: 'home',
	            template: __webpack_require__(270),
	            styles: [__webpack_require__(271)],
	            providers: [],
	            directives: [],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [])
	    ], Home);
	    return Home;
	})();
	exports.Home = Home;


/***/ },

/***/ 270:
/***/ function(module, exports) {

	module.exports = "<h3>Tango videos</h3>\n"

/***/ },

/***/ 271:
/***/ function(module, exports) {

	module.exports = ""

/***/ },

/***/ 272:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var http_1 = __webpack_require__(223);
	var videoPreview_1 = __webpack_require__(273);
	var VideoForm = (function () {
	    function VideoForm(http) {
	    }
	    VideoForm.prototype.fetchVideoData = function () {
	    };
	    VideoForm = __decorate([
	        core_1.Component({
	            selector: 'video-form',
	            template: __webpack_require__(277),
	            styles: [__webpack_require__(278)],
	            providers: [],
	            directives: [videoPreview_1.VideoPreview],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], VideoForm);
	    return VideoForm;
	})();
	exports.VideoForm = VideoForm;


/***/ },

/***/ 273:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var youtube_1 = __webpack_require__(274);
	var VideoInfo = (function () {
	    function VideoInfo(url, title, thumb) {
	        this.url = url;
	        this.title = title;
	        this.thumb = thumb;
	    }
	    return VideoInfo;
	})();
	var VideoPreview = (function () {
	    function VideoPreview(youtube) {
	        this.youtube = youtube;
	    }
	    VideoPreview.prototype.ngOnInit = function () {
	        var _this = this;
	        var id = this.src.split('?v=')[1];
	        this.youtube.getVideo(id).subscribe(function (data) {
	            //noinspection TypeScriptUnresolvedVariable
	            var snippet = data.items[0].snippet;
	            _this.videoInfo = new VideoInfo(id, snippet.title, snippet.thumbnails.default.url);
	        });
	    };
	    __decorate([
	        core_1.Input(), 
	        __metadata('design:type', String)
	    ], VideoPreview.prototype, "src", void 0);
	    VideoPreview = __decorate([
	        core_1.Component({
	            selector: 'video-preview',
	            template: __webpack_require__(275),
	            styles: [__webpack_require__(276)],
	            providers: [youtube_1.Youtube],
	            directives: [],
	        }), 
	        __metadata('design:paramtypes', [youtube_1.Youtube])
	    ], VideoPreview);
	    return VideoPreview;
	})();
	exports.VideoPreview = VideoPreview;


/***/ },

/***/ 274:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var http_1 = __webpack_require__(223);
	__webpack_require__(263);
	var BASE_URL = 'https://www.googleapis.com/youtube/v3/';
	var API_TOKEN = 'AIzaSyCW22iBpph-9DMs3rpHa3iXZDpTV0qsLCU';
	var Youtube = (function () {
	    function Youtube(http) {
	        this.http = http;
	    }
	    Youtube.prototype.makeRequest = function (path, params) {
	        params.append("key", API_TOKEN);
	        var url = ("" + BASE_URL) + path;
	        return this.http.get(url, { search: params })
	            .map(function (res) { return res.json(); });
	    };
	    Youtube.prototype.getVideo = function (id) {
	        var params = new http_1.URLSearchParams();
	        params.append("id", id);
	        params.append("part", "snippet");
	        return this.makeRequest("videos", params);
	    };
	    Youtube = __decorate([
	        core_1.Injectable(), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], Youtube);
	    return Youtube;
	})();
	exports.Youtube = Youtube;


/***/ },

/***/ 275:
/***/ function(module, exports) {

	module.exports = "<div *ngIf = \"videoInfo\" class = \"preview\">\n    <h1>{{videoInfo.title}}</h1>\n    <img [src]=\"videoInfo.thumb\" alt=\"\">\n</div>\n"

/***/ },

/***/ 276:
/***/ function(module, exports) {

	module.exports = ".preview {\n    width: 300px;\n    background-color: #eeeeee;\n    padding: 10px;\n    box-shadow: 2px 2px 2px #ddd;\n    border: 1px solid #aaa;\n}\n\nh1 {\n    font-size: 14px;\n}"

/***/ },

/***/ 277:
/***/ function(module, exports) {

	module.exports = "<label>Paste video URL: </label>\n\n<div>\n    <input #url value = \"https://www.youtube.com/watch?v=6D8uUFj8_4g\" />\n    <video-preview [src] = \"url.value\" ></video-preview>\n    <input type=\"submit\">\n</div>\n"

/***/ },

/***/ 278:
/***/ function(module, exports) {

	module.exports = ""

/***/ },

/***/ 279:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var core_2 = __webpack_require__(22);
	var ProfileService_1 = __webpack_require__(262);
	var common_1 = __webpack_require__(121);
	var UserProfile = (function () {
	    function UserProfile(profileService, builder) {
	        var _this = this;
	        this.profileService = profileService;
	        this.builder = builder;
	        this.authentificated = false;
	        this.profileService = profileService;
	        this.username = new common_1.Control("admin", common_1.Validators.required);
	        this.password = new common_1.Control("hashme", common_1.Validators.required);
	        this.loginForm = builder.group({
	            username: this.username,
	            password: this.password
	        });
	        profileService.getCurrentUser().subscribe(function (user) {
	            _this.user = user;
	            _this.authentificated = user.authentificated;
	        });
	    }
	    UserProfile.prototype.logout = function () {
	        var _this = this;
	        this.profileService.logout().subscribe(function (data) {
	            _this.authentificated = false;
	        });
	    };
	    UserProfile.prototype.authenticate = function () {
	        var _this = this;
	        this.profileService.login(this.loginForm.value).subscribe(function (user) {
	            _this.error = "";
	            _this.user = user;
	            _this.authentificated = user.authentificated;
	        }, function (data) {
	            //noinspection TypeScriptUnresolvedFunction
	            _this.error = data.json().type;
	        });
	    };
	    __decorate([
	        core_2.Input(), 
	        __metadata('design:type', Object)
	    ], UserProfile.prototype, "src", void 0);
	    UserProfile = __decorate([
	        core_1.Component({
	            selector: 'user-profile',
	            template: __webpack_require__(280),
	            styles: [__webpack_require__(281)],
	            directives: [common_1.FORM_DIRECTIVES]
	        }), 
	        __metadata('design:paramtypes', [ProfileService_1.CurrentUserService, common_1.FormBuilder])
	    ], UserProfile);
	    return UserProfile;
	})();
	exports.UserProfile = UserProfile;


/***/ },

/***/ 280:
/***/ function(module, exports) {

	module.exports = "<div class = \"user-profile\">\n<div *ngIf = \"authentificated\">\n    {{user.id}}\n    [<a class = \"log-out\" (click) = \"logout()\">Log out</a>]\n</div>\n<div *ngIf = \"!authentificated\">\n    <form [ngFormModel]=\"loginForm\" (ngSubmit)=\"authenticate()\">\n        <input type=\"text\" [ngFormControl] = \"username\">\n        <input type=\"text\" [ngFormControl] = \"password\">\n        <input type=\"submit\" [disabled]=\"!loginForm.valid\">\n    </form>\n    <div>{{error}}</div>\n</div>\n</div>"

/***/ },

/***/ 281:
/***/ function(module, exports) {

	module.exports = ".user-profile {\n    position: absolute;\n    right: 0;\n    top: 0;\n    width: 250px;\n    background-color: #eee;\n    color: #000;\n    padding: 10px;\n    box-shadow: 0 2px 2px #ddd;\n}\n\n.log-out {\n    text-decoration: underline;\n    cursor: pointer;\n}"

/***/ },

/***/ 282:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var DancerService_1 = __webpack_require__(283);
	var router_1 = __webpack_require__(238);
	var videos_1 = __webpack_require__(284);
	var Dancer = (function () {
	    function Dancer(dancerService, params) {
	        this.dancerService = dancerService;
	        this.id = params.get('id');
	    }
	    Dancer.prototype.ngOnInit = function () {
	        var _this = this;
	        this.dancerService.get(this.id).subscribe(function (data) {
	            _this.dancer = data;
	        });
	    };
	    Dancer = __decorate([
	        core_1.Component({
	            selector: 'dancer',
	            template: __webpack_require__(293),
	            styles: [__webpack_require__(294)],
	            providers: [],
	            directives: [videos_1.Videos],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [DancerService_1.DancerService, router_1.RouteParams])
	    ], Dancer);
	    return Dancer;
	})();
	exports.Dancer = Dancer;


/***/ },

/***/ 283:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var http_1 = __webpack_require__(223);
	__webpack_require__(263);
	var DancerService = (function () {
	    function DancerService(http) {
	        this.http = http;
	    }
	    DancerService.prototype.makeRequest = function (url) {
	        url = '/api/dancers' + url;
	        return this.http.get(url).map(function (res) { return res.json(); });
	    };
	    DancerService.prototype.get = function (id) {
	        return this.makeRequest("/" + id);
	    };
	    DancerService = __decorate([
	        core_1.Injectable(), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], DancerService);
	    return DancerService;
	})();
	exports.DancerService = DancerService;


/***/ },

/***/ 284:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var videoInfo_1 = __webpack_require__(285);
	var Videos = (function () {
	    function Videos() {
	        this.videos = [];
	    }
	    Videos.prototype.getThumbnail = function (id) {
	        return "https://i.ytimg.com/vi/" + id + "/hqdefault.jpg";
	    };
	    __decorate([
	        core_1.Input(), 
	        __metadata('design:type', Array)
	    ], Videos.prototype, "videos", void 0);
	    Videos = __decorate([
	        core_1.Component({
	            selector: 'videos',
	            template: __webpack_require__(291),
	            styles: [__webpack_require__(292)],
	            providers: [],
	            directives: [videoInfo_1.VideoInfo],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [])
	    ], Videos);
	    return Videos;
	})();
	exports.Videos = Videos;


/***/ },

/***/ 285:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var dancer_tile_1 = __webpack_require__(286);
	var VideoService_1 = __webpack_require__(267);
	var VideoInfo = (function () {
	    function VideoInfo(videoService) {
	        this.videoService = videoService;
	    }
	    VideoInfo.prototype.addDancer = function (name) {
	        var _this = this;
	        this.videoService.addDancer(this.video.id, name).subscribe(function (data) {
	            _this.video.dancers = data;
	        });
	    };
	    VideoInfo.prototype.removeDancer = function (name) {
	        var _this = this;
	        this.videoService.removeDancer(this.video.id, name).subscribe(function (data) {
	            _this.video.dancers = data;
	        });
	    };
	    __decorate([
	        core_1.Input(), 
	        __metadata('design:type', Object)
	    ], VideoInfo.prototype, "video", void 0);
	    VideoInfo = __decorate([
	        core_1.Component({
	            selector: 'video-info',
	            template: __webpack_require__(289),
	            styles: [__webpack_require__(290)],
	            providers: [],
	            directives: [dancer_tile_1.DancerTile],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [VideoService_1.VideoService])
	    ], VideoInfo);
	    return VideoInfo;
	})();
	exports.VideoInfo = VideoInfo;


/***/ },

/***/ 286:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var router_1 = __webpack_require__(238);
	var DancerTile = (function () {
	    function DancerTile() {
	        this.dancers = [];
	        this.add = new core_1.EventEmitter();
	        this.remove = new core_1.EventEmitter();
	    }
	    DancerTile.prototype.addDancer = function (dancer) {
	        // TODO: Proper escaping
	        dancer = dancer.replace('/', ' ');
	        this.add.emit(dancer);
	    };
	    DancerTile.prototype.removeDancer = function (event, dancer) {
	        event.preventDefault();
	        event.stopPropagation();
	        this.remove.emit(dancer);
	    };
	    DancerTile.prototype.addNewInput = function () {
	        this.dancers = this.dancers || [];
	        if (this.dancers.filter(function (i) { return i == ''; }).length == 0) {
	            this.dancers.push('');
	        }
	    };
	    __decorate([
	        core_1.Input(), 
	        __metadata('design:type', Object)
	    ], DancerTile.prototype, "dancers", void 0);
	    __decorate([
	        core_1.Output(), 
	        __metadata('design:type', Object)
	    ], DancerTile.prototype, "add", void 0);
	    __decorate([
	        core_1.Output(), 
	        __metadata('design:type', Object)
	    ], DancerTile.prototype, "remove", void 0);
	    DancerTile = __decorate([
	        core_1.Component({
	            selector: 'dancer-tile',
	            template: __webpack_require__(287),
	            styles: [__webpack_require__(288)],
	            providers: [],
	            directives: [router_1.ROUTER_DIRECTIVES],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [])
	    ], DancerTile);
	    return DancerTile;
	})();
	exports.DancerTile = DancerTile;


/***/ },

/***/ 287:
/***/ function(module, exports) {

	module.exports = "<div *ngIf=\"saving\">\n    Please wait, saving your changes.\n</div>\n<div *ngIf=\"!saving\">\n    <div *ngFor=\"#dancer of dancers\" class=\"dancer-tile\">\n        <div *ngIf=\"dancer == ''\">\n            <input #f type=\"text\"><input type=\"submit\" value=\"Add dancer\" (click)=\"addDancer(f.value)\">\n        </div>\n\n        <div *ngIf=\"dancer != ''\" class = \"dancer\" [routerLink]=\"['Dancers', {id: dancer}]\">\n            <div class=\"image\"></div>\n            <div class=\"name\">{{dancer}}</div>\n            <div class=\"remove\" (click) = \"removeDancer($event, dancer)\">&times;</div>\n        </div>\n    </div>\n    <div (click)=\"addNewInput()\">Add dancer</div>\n</div>"

/***/ },

/***/ 288:
/***/ function(module, exports) {

	module.exports = ".dancer-tile {\n    margin-bottom: 5px;\n}\n\n.dancer-tile:hover {\n    background: #eee;\n    cursor: pointer;\n    border-radius: 16px;\n}\n\n.dancer-tile .name {\n    margin-left: 10px;\n}\n\n.dancer-tile:hover .remove {\n    display: inline-block;\n}\n.dancer-tile:hover .remove:hover {\n    background-color: #999;\n    color: white;\n}\n\n.dancer-tile .remove {\n    float: right;\n    height: 32px;\n    line-height: 32px;\n    background-color: #bbbbbb;\n    width: 32px;\n    text-align: center;\n    border-radius: 16px;\n    display: none;\n}\n\n.dancer-tile .image {\n    width: 32px;\n    height: 32px;\n    background-color: #f90;\n    border: 2px #000 solid;\n    border-radius: 50%;\n\n}\n.dancer-tile .image, .dancer-tile .name{\n    display: inline-block;\n    vertical-align: middle;\n}"

/***/ },

/***/ 289:
/***/ function(module, exports) {

	module.exports = "<div class=\"data\">\n    <h2>{{video.title}}</h2>\n    <div>{{video.publishedAt}}</div>\n\n    <h3>Dancers:</h3>\n    <dancer-tile [dancers]=\"video.dancers\" (add)=\"addDancer($event)\" (remove)=\"removeDancer($event)\" ></dancer-tile>\n    <h3>Songs:</h3>\n    <div *ngFor=\"#song of video.songs\" class=\"video\">\n        <img [src]=\"getThumbnail(video.id)\">\n    </div>\n\n    <h3>Event:</h3>\n    <div *ngFor=\"#event of video.event\" class=\"video\">\n        <img [src]=\"getThumbnail(video.id)\">\n    </div>\n\n    <h3>Location:</h3>\n    <div *ngFor=\"#performer of video.location\" class=\"video\">\n        <img [src]=\"getThumbnail(video.id)\">\n    </div>\n</div>"

/***/ },

/***/ 290:
/***/ function(module, exports) {

	module.exports = "h2 {\n    margin: 0;\n    padding: 0;\n    font-size: 20px;\n    font-weight: 300;\n}\n.data {\n    padding: 0 10px;\n}\n\nh3 {\n    margin-top: 5px;\n    margin-bottom: 5px;\n}"

/***/ },

/***/ 291:
/***/ function(module, exports) {

	module.exports = "<div class=\"videos\">\n    <div *ngFor=\"#video of videos\" class=\"video\">\n        <div class=\"thumb\">\n            <img [src]=\"getThumbnail(video.id)\">\n        </div>\n        <video-info [video] = video></video-info>\n    </div>\n</div>"

/***/ },

/***/ 292:
/***/ function(module, exports) {

	module.exports = ".videos {\n    padding: 20px;\n}\n\n.video {\n    padding: 20px;\n    width: 1024px;\n}\n\n.thumb {\n    width: 480px;\n}\n\nvideo-info {\n    width: 500px;\n}\n.thumb, .data, video-info {\n    display: inline-block;\n    vertical-align: top;\n}\n\nh2, h3 {\n    margin: 0;\n    padding: 0;\n}"

/***/ },

/***/ 293:
/***/ function(module, exports) {

	module.exports = "<div *ngIf = \"dancer\">\n    <h1>{{dancer.name}}</h1>\n    <videos [videos] = \"dancer.videos\"></videos>\n</div>\n"

/***/ },

/***/ 294:
/***/ function(module, exports) {

	module.exports = ""

/***/ },

/***/ 295:
/***/ function(module, exports, __webpack_require__) {

	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(22);
	var videos_1 = __webpack_require__(284);
	var VideoService_1 = __webpack_require__(267);
	var Explore = (function () {
	    function Explore(videoService) {
	        var _this = this;
	        this.videoService = videoService;
	        this.videos = [];
	        videoService.list().subscribe(function (videos) {
	            _this.videos = videos;
	        });
	    }
	    Explore = __decorate([
	        core_1.Component({
	            selector: 'explore',
	            template: __webpack_require__(296),
	            styles: [__webpack_require__(297)],
	            providers: [],
	            directives: [videos_1.Videos],
	            pipes: []
	        }), 
	        __metadata('design:paramtypes', [VideoService_1.VideoService])
	    ], Explore);
	    return Explore;
	})();
	exports.Explore = Explore;


/***/ },

/***/ 296:
/***/ function(module, exports) {

	module.exports = "<videos [videos] = \"videos\"></videos>"

/***/ },

/***/ 297:
/***/ function(module, exports) {

	module.exports = ""

/***/ },

/***/ 298:
/***/ function(module, exports) {

	module.exports = "<div class=\"header\">\n    <h1>Tango Videos</h1>\n    <div class=\"profile\">\n        <user-profile></user-profile>\n    </div>\n</div>\n<div>\n    <a [routerLink]=\"['/Videos']\">Videos</a>\n    <a [routerLink]=\"['/Home']\">Home</a>\n    <a [routerLink]=\"['/AddVideo']\">Add Video</a>\n</div>\n<div>\n    <router-outlet></router-outlet>\n</div>\n"

/***/ }

});