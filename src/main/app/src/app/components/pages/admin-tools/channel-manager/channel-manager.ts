import {Component} from 'angular2/core';
import {AddableField} from "../../../common/addable-field/addable-field";
import {Observable} from "rxjs";
import {ChannelService} from "../../../../services/ChannelService";
import {Icon} from "../../../common/icon/icon";

interface Channel {
    title: String,
    id: String
}
@Component({
    selector: 'channel-manager',
    template: require('./channel-manager.html'),
    styles: [require('./channel-manager.css')],
    providers: [],
    directives: [AddableField, Icon],
    pipes: []
})
export class ChannelManager {
    channels:Array<Channel> = [];

    addChannel(channelId) {
        this.channelService.add(channelId.trim()).subscribe(this.setChannels.bind(this))
    }

    fetchLatestVideos(channelId){
        this.channelService.fetchLatestVideos(channelId.trim()).subscribe(this.setChannels.bind(this))
    }

    setChannels(channels) {
        this.channels = channels;
    }
    switchAutoupdate( channel, value ){
        this.channelService.setAutoUpdate(channel.id, value).subscribe(()=>{});
    }

    constructor(private channelService:ChannelService) {
        channelService.list().subscribe(this.setChannels.bind(this));
    }

}
