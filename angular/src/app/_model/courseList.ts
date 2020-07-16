import {BaseListEntity} from "./base-list-entity";

export class CourseList extends BaseListEntity{
  name:string;
  description:string;
  maxCountStudent:number;
  startDate:string;
  endDate:string;
  status:string;
}
