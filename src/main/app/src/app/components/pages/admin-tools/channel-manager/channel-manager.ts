import {Component} from '@angular/core';
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
    updated: number = 0;

    addChannel(channelId) {
        this.channelService.add(channelId.trim()).subscribe(this.setChannels.bind(this))
    }

    fetchLatestVideos(channelId) {
        this.channelService.fetchLatestVideos(channelId.trim()).subscribe(this.setChannels.bind(this))
    }

    updateAll() {
        this.channelService.updateAll().subscribe((count)=> {
            this.updated = count;
        });
    }

    setChannels(channels) {
        this.channels = channels;
    }

    switchAutoupdate(channel, value) {
        this.channelService.setAutoUpdate(channel.id, value).subscribe(()=> {
        });
    }

    constructor(private channelService:ChannelService) {
        channelService.list().subscribe(this.setChannels.bind(this));
    }

}
