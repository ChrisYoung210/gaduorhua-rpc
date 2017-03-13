package com.young.gaduorhua.rpc

import java.lang.reflect.Method

/**
  * Created by Young on 16-11-13.
  */
object ImplicityInstance {

  implicit val methodOrder = new Ordering[Method] {
    override def compare(x: Method, y: Method): Int = {
      x.getName.compareTo(y.getName) match {
        case 0 => {
          x.getParameterTypes.lengthCompare(y.getParameterTypes.length) match {
            case 0 => var result = 0
              for (i <- x.getParameterTypes.indices) {
                if (result == 0) {
                  result = x.getParameterTypes.apply(i).getName.compareTo(y.getParameterTypes.apply(i).getName)
                }
              }
              result
            case x: Int => x
          }
        }
        case x: Int => x
      }
    }
  }

}

