import {Directive, Input, ElementRef} from 'angular2/core';

@Directive({
    selector: '[focus]'
})
export class Focus {
    @Input()
    focus:boolean;
    constructor(private el: ElementRef) {}
    protected ngOnChanges() {
        this.el.nativeElement.focus();
    }
}
