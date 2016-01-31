webpackJsonp([0],{

/***/ 0:
/***/ function(module, exports, __webpack_require__) {

	var browser_1 = __webpack_require__(1);
	var http_1 = __webpack_require__(223);
	var router_1 = __webpack_require__(238);
	var seed_app_1 = __webpack_require__(262);
	browser_1.bootstrap(seed_app_1.SeedApp, [http_1.HTTP_PROVIDERS, router_1.ROUTER_PROVIDERS])
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
	var router_1 = __webpack_require__(238);
	var home_1 = __webpack_require__(263);
	var about_1 = __webpack_require__(264);
	var repo_browser_1 = __webpack_require__(265);
	var SeedApp = (function () {
	    function SeedApp() {
	    }
	    SeedApp = __decorate([
	        core_1.Component({
	            selector: 'tango-videos',
	            providers: [],
	            templateUrl: 'app/tango-videos.html',
	            directives: [router_1.ROUTER_DIRECTIVES],
	            pipes: []
	        }),
	        router_1.RouteConfig([
	            new router_1.Route({ path: '/home', component: home_1.Home, name: 'Home', useAsDefault: true }),
	            new router_1.Route({ path: '/videoForm', component: about_1.About, name: 'VideoForm' }),
	            new router_1.Route({ path: '/github/...', component: repo_browser_1.RepoBrowser, name: 'RepoBrowser' })
	        ]),
	        __metadata('design:paramtypes', [])
	    ], SeedApp);
	    return SeedApp;
	})();
	exports.SeedApp = SeedApp;


/***/ },

/***/ 263:
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
	            templateUrl: 'app/components/home/home.html',
	            styleUrls: ['app/components/home/home.css'],
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

/***/ 264:
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
	var About = (function () {
	    function About(http) {
	    }
	    About = __decorate([
	        core_1.Component({
	            selector: 'videoForm',
	            templateUrl: 'app/components/videoForm/videoForm.html',
	            styleUrls: ['app/components/videoForm/videoForm.css'],
	            providers: [],
	            directives: [],
	            pipes: []
	        }),
	        __metadata('design:paramtypes', [http_1.Http])
	    ], About);
	    return About;
	})();
	exports.About = About;


/***/ },

/***/ 265:
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
	var repo_list_1 = __webpack_require__(266);
	var repo_detail_1 = __webpack_require__(272);
	var github_1 = __webpack_require__(267);
	var RepoBrowser = (function () {
	    function RepoBrowser(router, github) {
	        this.router = router;
	        this.github = github;
	    }
	    RepoBrowser.prototype.searchForOrg = function (orgName) {
	        var _this = this;
	        this.github.getOrg(orgName)
	            .subscribe(function (_a) {
	            var name = _a.name;
	            console.log(name);
	            _this.router.navigate(['RepoList', { org: orgName }]);
	        });
	    };
	    RepoBrowser = __decorate([
	        core_1.Component({
	            selector: 'repo-browser',
	            templateUrl: 'app/components/repo-browser/repo-browser.html',
	            styleUrls: ['app/components/repo-browser/repo-browser.css'],
	            providers: [github_1.Github],
	            directives: [router_1.ROUTER_DIRECTIVES],
	            pipes: []
	        }),
	        router_1.RouteConfig([
	            new router_1.Route({ path: '/:org', component: repo_list_1.RepoList, name: 'RepoList' }),
	            new router_1.Route({ path: '/:org/:name', component: repo_detail_1.RepoDetail, name: 'RepoDetail' })
	        ]),
	        __metadata('design:paramtypes', [router_1.Router, github_1.Github])
	    ], RepoBrowser);
	    return RepoBrowser;
	})();
	exports.RepoBrowser = RepoBrowser;


/***/ },

/***/ 266:
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
	var github_1 = __webpack_require__(267);
	var router_1 = __webpack_require__(238);
	var RepoList = (function () {
	    function RepoList(github, params) {
	        this.repos = github.getReposForOrg(params.get('org'));
	    }
	    RepoList = __decorate([
	        core_1.Component({
	            selector: 'repo-list',
	            templateUrl: 'app/components/repo-list/repo-list.html',
	            styleUrls: ['app/components/repo-list/repo-list.css'],
	            providers: [],
	            directives: [router_1.ROUTER_DIRECTIVES],
	            pipes: []
	        }),
	        __metadata('design:paramtypes', [github_1.Github, router_1.RouteParams])
	    ], RepoList);
	    return RepoList;
	})();
	exports.RepoList = RepoList;


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
	__webpack_require__(268);
	var Github = (function () {
	    function Github(http) {
	        this.http = http;
	    }
	    Github.prototype.getOrg = function (org) {
	        return this.makeRequest("orgs/" + org);
	    };
	    Github.prototype.getReposForOrg = function (org) {
	        return this.makeRequest("orgs/" + org + "/repos");
	    };
	    Github.prototype.getRepoForOrg = function (org, repo) {
	        return this.makeRequest("repos/" + org + "/" + repo);
	    };
	    Github.prototype.makeRequest = function (path) {
	        var params = new http_1.URLSearchParams();
	        params.set('per_page', '100');
	        var url = "https://api.github.com/" + path;
	        return this.http.get(url, { search: params })
	            .map(function (res) { return res.json(); });
	    };
	    Github = __decorate([
	        core_1.Injectable(),
	        __metadata('design:paramtypes', [http_1.Http])
	    ], Github);
	    return Github;
	})();
	exports.Github = Github;


/***/ },

/***/ 268:
/***/ function(module, exports, __webpack_require__) {

	var Observable_1 = __webpack_require__(65);
	var map_1 = __webpack_require__(269);
	Observable_1.Observable.prototype.map = map_1.map;
	//# sourceMappingURL=map.js.map

/***/ },

/***/ 269:
/***/ function(module, exports, __webpack_require__) {

	var __extends = (this && this.__extends) || function (d, b) {
	    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
	    function __() { this.constructor = d; }
	    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
	};
	var Subscriber_1 = __webpack_require__(66);
	var tryCatch_1 = __webpack_require__(270);
	var errorObject_1 = __webpack_require__(271);
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

/***/ 270:
/***/ function(module, exports, __webpack_require__) {

	var errorObject_1 = __webpack_require__(271);
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

/***/ 271:
/***/ function(module, exports) {

	exports.errorObject = { e: {} };
	//# sourceMappingURL=errorObject.js.map

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
	var router_1 = __webpack_require__(238);
	var github_1 = __webpack_require__(267);
	var RepoDetail = (function () {
	    function RepoDetail(routeParams, github) {
	        var _this = this;
	        this.repoDetails = {};
	        github.getRepoForOrg(routeParams.get('org'), routeParams.get('name'))
	            .subscribe(function (repoDetails) {
	            _this.repoDetails = repoDetails;
	        });
	    }
	    RepoDetail = __decorate([
	        core_1.Component({
	            selector: 'repo-detail',
	            templateUrl: 'app/components/repo-detail/repo-detail.html',
	            styleUrls: ['app/components/repo-detail/repo-detail.css'],
	            providers: [],
	            directives: [router_1.ROUTER_DIRECTIVES],
	            pipes: []
	        }),
	        __metadata('design:paramtypes', [router_1.RouteParams, github_1.Github])
	    ], RepoDetail);
	    return RepoDetail;
	})();
	exports.RepoDetail = RepoDetail;


/***/ }

});
