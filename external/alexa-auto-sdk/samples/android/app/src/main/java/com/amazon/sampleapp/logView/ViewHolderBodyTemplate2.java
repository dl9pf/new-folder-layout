/*
 * Copyright 2017-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.sampleapp.logView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.sampleapp.R;

public class ViewHolderBodyTemplate2 extends RecyclerView.ViewHolder {
    private final TextView mMainTitle;
    private final TextView mSubTitle;
    private final TextView mTextField;
    private final ImageView mImage;

    public ViewHolderBodyTemplate2(View v ) {
        super( v );
        mMainTitle = v.findViewById( R.id.mainTitle );
        mSubTitle = v.findViewById( R.id.subTitle );
        mTextField = v.findViewById( R.id.textField );
        mImage = v.findViewById( R.id.image );
    }

    public TextView getMainTitle() { return mMainTitle; }
    public TextView getSubTitle() { return mSubTitle; }
    public TextView getTextField() { return mTextField; }
    public ImageView getImage() { return mImage; }
}

