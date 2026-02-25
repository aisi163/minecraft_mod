/*
MIT License

Copyright (c) 2026 aisi163

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.aisi163.npc_builder.NPCEntity.exceptions;


public class BehaviorInheritanceException extends Exception {

    private int exception_type_code = 0;
    private String err_entity_register_name = "";
    private BehaviorInheritanceExceptionEnum exception_type = null;

    public BehaviorInheritanceException(BehaviorInheritanceExceptionEnum exception_type,
                                        String err_entity_register_name) {

           super(BehaviorInheritanceExceptionStr.to_str(exception_type,err_entity_register_name));
           this.exception_type_code = exception_type.ordinal();
           this.exception_type =exception_type;
           this.err_entity_register_name = err_entity_register_name;
    };

    public int getErr_type() {
        return this.exception_type_code;
    }

    public String getErr_entity_register_name() {
        return this.err_entity_register_name;
    }

    public String toUserFriendlyMessage() {
        return BehaviorInheritanceExceptionStr.toUserStr(this.exception_type,this.getErr_entity_register_name());
    }
}


