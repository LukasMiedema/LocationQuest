import Vue = require('vue')
import VueRouter = require('vue-router')
import * as vts from 'vue-typescript-component'



@vts.component()
export default class CounterTs extends Vue implements ChildListener {

}
