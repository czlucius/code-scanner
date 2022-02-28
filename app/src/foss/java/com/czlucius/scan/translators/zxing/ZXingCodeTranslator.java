/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2021 Lucius Chee Zihan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.translators.zxing;

import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.Type;
import com.czlucius.scan.translators.CodeTranslator;
import com.czlucius.scan.translators.TranslationException;
import com.google.zxing.Result;

public class ZXingCodeTranslator implements CodeTranslator<Result> {
    @Override
    public Code convert(Result from) throws TranslationException {

        Code code = new Code()
        return null;
    }
}
