import { YoutubeThumb} from "./youtubeThumb";

describe('YoutubeThumb', ()=> {
    it('Sets proper src', ()=> {
        var needsPermission = new YoutubeThumb();
        expect(needsPermission.src).toBe('');
        needsPermission.youtubeThumb= 'a';
        expect(needsPermission.src).toBe('https://i.ytimg.com/vi/a/mqdefault.jpg');
    })
});