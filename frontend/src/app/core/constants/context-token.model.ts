import {HttpContextToken} from '@angular/common/http';


export const SKIP_AUTH = new HttpContextToken<boolean>(() => false)
export const USE_HEADER = new HttpContextToken<boolean>(() => false)
