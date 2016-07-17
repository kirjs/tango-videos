import {
    it,
    inject,
    describe,
    beforeEachProviders
    expect
} from '@angular/core/testing';
import {NeedsPermission} from "./needsPermission";
import {CurrentUserService} from "../../../services/ProfileService";

// TODO: Test properly with dependency injection.
describe('needsPermission', ()=> {
    it('Can be instantiated', ()=> {

        var currentUserService = ({
            permissionObservable: {
                subscribe: (callback)=> {
                    callback({a: 1});
                }
            }
        } as CurrentUserService);

        var needsPermission = new NeedsPermission(currentUserService);
        expect(needsPermission.hidden).toBe(true);
        needsPermission.needsPermission = 'a';
        expect(needsPermission.hidden).toBe(false);
        needsPermission.needsPermission = 'b';
        expect(needsPermission.hidden).toBe(true);
    })
});