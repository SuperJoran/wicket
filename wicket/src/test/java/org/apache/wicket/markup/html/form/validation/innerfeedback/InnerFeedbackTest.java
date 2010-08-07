/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.form.validation.innerfeedback;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for Wicket-2974
 */
public class InnerFeedbackTest
{

	private WicketTester tester;

	@Before
	public void before()
	{
		tester = new WicketTester(new MockApplication()
		{
			@Override
			protected void init()
			{
				super.init();

				// we don't care about FormComponentFeedbackBorder's 'errorindicator'
				getDebugSettings().setComponentUseCheck(false);
			}

			/**
			 * @see org.apache.wicket.Application#getHomePage()
			 */
			@Override
			public Class<HomePage> getHomePage()
			{
				return HomePage.class;
			}
		});
	}

	@Test
	public void innerFeedback()
	{
		tester.startPage(HomePage.class);

		// page's feedback
		tester.assertInfoMessages(new String[] { "info on field", "page onbeforerender" });

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		// feedback message for LocalizedFeedbackBorder
		// without the fix the same error message was reported for the page's feedback panel too
		tester.assertErrorMessages(new String[] { "Field 'field' is required." });

		// page's feedback
		tester.assertInfoMessages(new String[] { "page onbeforerender" });

		formTester = tester.newFormTester("form");
		formTester.setValue("fieldborder:border:fieldborder_body:field", "some text");
		formTester.submit();

		tester.assertErrorMessages(new String[0]);

		// page's feedback
		tester.assertInfoMessages(new String[] { "form submitted", "page onbeforerender" });
	}
}
